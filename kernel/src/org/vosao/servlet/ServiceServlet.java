package org.vosao.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.jabsorb.JSONRPCResult;
import org.jabsorb.JSONRPCServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.vosao.common.VosaoContext;
import org.vosao.filter.AuthenticationFilter;

public class ServiceServlet extends JSONRPCServlet {

	private static final Log log = LogFactory.getLog(ServiceServlet.class);
	
	/**
     * The size of the buffer used for reading requests
     */
    private final static int buf_size = 4096;
    private static int GZIP_THRESHOLD = 200;
    
	/**
     * Called when a JSON-RPC requests comes in.
     * Looks in the session for a JSONRPCBridge and if not found there,
     * uses the global bridge; then passes off the
     * JSON-PRC call to be handled by the JSONRPCBridge found.
     *
     * @param request servlet request from browser.
     * @param response servlet response to browser.
     *
     * @throws IOException if an IOException occurs during processing.
     */
    public void service(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // Use protected method in case someone wants to override it
        JSONRPCBridge json_bridge = findBridge(request);

        // Decode using the charset in the request if it exists otherwise
        // use UTF-8 as this is what all browser implementations use.
        // The JSON-RPC-Java JavaScript client is ASCII clean so it
        // although here we can correctly handle data from other clients
        // that do not escape non ASCII data
        String charset = request.getCharacterEncoding();
        if (charset == null) {
            charset = "UTF-8";
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                request.getInputStream(), charset));

        String receiveString = (String) request
                .getAttribute("_jabsorb_beenHere");

        // if JSON data is found in a special request attribute, it means
        // that a continuation was used and this request is being retried
        // as a consequence of a Jetty continuation
        // see http://blogs.webtide.com/gregw/2007/11/18/1195421880000.html
        if (receiveString == null) {
            // Read the request
            CharArrayWriter data = new CharArrayWriter();
            char buf[] = new char[buf_size];
            int ret;
            while ((ret = in.read(buf, 0, buf_size)) != -1) {
                data.write(buf, 0, ret);
            }
            receiveString = data.toString();

            // save the json-rpc data in a special request attribute, in case a jetty 
            // continuation exception (org.mortbay.jetty.RetryRequest) is thrown and this 
            // request is retried by the container
            request.setAttribute("_jabsorb_beenHere", receiveString);
        } else {
            log.debug("jetty continuation resumed...");
        }

  /*      if (log.isDebugEnabled()) {
            log.debug("receive: " + receiveString);
        }*/

        // Process the request
        JSONObject json_req;
        JSONRPCResult json_res;
        try {
            json_req = new JSONObject(receiveString);
            json_res = json_bridge.call(new Object[] { request,
                    response }, json_req);
        } catch (JSONException e) {
            log.error("can't parse call" + receiveString, e);
            json_res = new JSONRPCResult(JSONRPCResult.CODE_ERR_PARSE,
                    null, JSONRPCResult.MSG_ERR_PARSE);
        }

        String sendString = json_res.toString();

        // dump the received string
 /*       if (log.isDebugEnabled()) {
            log.debug("send: " + sendString);
        } */

        // Write the response
        byte[] bout = sendString.getBytes("UTF-8");

        // handle gzipping of the response if it is turned on
        if (GZIP_THRESHOLD != -1) {
            // if the request header says that the browser can take gzip compressed output, then gzip the output
            // but only if the response is large enough to warrant it and if the resultant compressed output is
            // actually smaller.
            if (acceptsGzip(request)) {
                if (bout.length > GZIP_THRESHOLD) {
                    byte[] gzippedOut = gzip(bout);
                    log.debug("gzipping! original size =  "
                            + bout.length + "  gzipped size = "
                            + gzippedOut.length);

                    // if gzip didn't actually help, abort
                    if (bout.length <= gzippedOut.length) {
                        log
                                .warn("gzipping resulted in a larger output size!  "
                                        + "aborting (sending non-gzipped response)... "
                                        + "you may want to increase the gzip threshold if this happens a lot!"
                                        + " original size = "
                                        + bout.length
                                        + "  gzipped size = "
                                        + gzippedOut.length);
                    } else {
                        // go with the gzipped output
                        bout = gzippedOut;
                        response.addHeader("Content-Encoding", "gzip");
                    }
                } else {
                    log
                            .debug("not gzipping because size is "
                                    + bout.length
                                    + " (less than the GZIP_THRESHOLD of "
                                    + GZIP_THRESHOLD
                                    + " bytes)");
                }
            } else {
                // this should be rare with modern user agents
                log
                        .debug("not gzipping because user agent doesn't accept gzip encoding...");
            }
        }

        // Encode using UTF-8, although We are actually ASCII clean as
        // all unicode data is JSON escaped using backslash u. This is
        // less data efficient for foreign character sets but it is
        // needed to support naughty browsers such as Konqueror and Safari
        // which do not honour the charset set in the response
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = response.getOutputStream();

        response.setIntHeader("Content-Length", bout.length);

        VosaoContext.getInstance().getSession().save(response);
        
        out.write(bout);
        out.flush();
        out.close();
    }
	
	@Override
	protected JSONRPCBridge findBridge(HttpServletRequest request) {
		JSONRPCBridge bridge = new JSONRPCBridge();
    	VosaoContext ctx = VosaoContext.getInstance();
		
    	boolean isLoggedIn = ctx.getSession().getString(
				AuthenticationFilter.USER_SESSION_ATTR) != null;

    	VosaoContext.getInstance().getFrontService().register(bridge);

    	if (isLoggedIn && ctx.getUser() != null	&& !ctx.getUser().isSiteUser()) {
        	VosaoContext.getInstance().getBackService().register(bridge);
        }
        return bridge;
    }

	/**
     * Can browser accept gzip encoding?
     *
     * @param request browser request object.
     * @return true if gzip encoding accepted.
     */
    private boolean acceptsGzip(HttpServletRequest request) {
        // can browser accept gzip encoding?
        String ae = request.getHeader("accept-encoding");
        return ae != null && ae.indexOf("gzip") != -1;
    }

    /**
     * Gzip something.
     *
     * @param in original content
     * @return size gzipped content
     */
    private byte[] gzip(byte[] in) {
        if (in != null && in.length > 0) {
            long tstart = System.currentTimeMillis();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            try {
                GZIPOutputStream gout = new GZIPOutputStream(bout);
                gout.write(in);
                gout.flush();
                gout.close();
                if (log.isDebugEnabled()) {
                    log.debug("gzipping took "
                            + (System.currentTimeMillis() - tstart)
                            + " msec");
                }
                return bout.toByteArray();
            } catch (IOException io) {
                log.error("io exception gzipping byte array", io);
            }
        }
        return new byte[0];
    }
}
