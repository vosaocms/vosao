package org.vosao.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import javax.faces.component.html.HtmlMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.vosao.entity.FormEntity;
import org.vosao.service.FormService;
import org.vosao.service.ServiceResponse;

public class FormServiceImpl extends AbstractServiceImpl 
		implements FormService {

	@Override
	public ServiceResponse send(final String name, Map<String, String> params) {
		FormEntity form = getDao().getFormDao().getByName(name);
		if (form == null) {
			return new ServiceResponse("error","Form not found");
		}
		String msgBody = createLetter(params);
		String subject = form.getLetterSubject();
		String fromAddress = getBusiness().getConfigBusiness().getSiteEmail();
		String fromText = getBusiness().getConfigBusiness().getSiteDomain() 
				+ " admin";
		String toAddress = form.getEmail();
		String error = sendEmail(msgBody, subject, fromAddress, fromText, 
					toAddress);
		if (error == null) {
			return new ServiceResponse("success","Form sended to email address " 
					+ form.getEmail());
		}
		else {
			return new ServiceResponse("error", error);
		}
	}
	
	private String createLetter(final Map<String, String> params) {
		StringBuilder result = new StringBuilder();
		result.append("<table>");
		for (String key : params.keySet()) {
			result.append("<tr><td>").append(key).append("</td><td>")
				.append(params.get(key)).append("</td></tr>");
		}
		return result.toString();
	}

	private String sendEmail(final String htmlBody, final String subject, 
			final String fromAddress, final String fromText, 
			final String toAddress) {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
        	Multipart mp = new MimeMultipart();
        	MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);
        	MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromAddress, fromText));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(toAddress, toAddress));
            msg.setSubject(subject);
            msg.setContent(mp);
            Transport.send(msg);
            return null;
        } catch (AddressException e) {
            return e.getMessage();
        } catch (MessagingException e) {
            return e.getMessage();
        } catch (UnsupportedEncodingException e) {
        	return e.getMessage();
		}		
	}
	

}
