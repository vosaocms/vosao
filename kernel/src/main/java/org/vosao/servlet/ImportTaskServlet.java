package org.vosao.servlet;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.vosao.business.CurrentUser;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.impl.imex.dao.DaoTaskAdapter;
import org.vosao.business.impl.imex.dao.DaoTaskFinishedException;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.helper.UserHelper;

import com.google.appengine.api.labs.taskqueue.Queue;

public class ImportTaskServlet extends BaseSpringServlet {

	private static final Log logger = LogFactory
			.getLog(ImportTaskServlet.class);

	public static final String IMPORT_TASK_URL = "/_ah/queue/import";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doImport(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doImport(request, response);
	}

	public void doImport(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filename = request.getParameter("filename");
		try {
			CurrentUser.setInstance(UserHelper.ADMIN);
			int start = Integer.valueOf(request.getParameter("start"));
			getDaoTaskAdapter().setStart(start);
			logger.info("Import " + filename + " " + start);
			FolderEntity folder = getBusiness().getFolderBusiness()
				.findFolderByPath(getBusiness().getFolderBusiness().getTree(), 
						"/tmp").getEntity();
			FileEntity file = getDao().getFileDao().getByName(folder.getId(), 
					filename);
			if (file == null) {
				return;
			}
			byte[] data = getDao().getFileDao().getFileContent(file);
			ByteArrayInputStream inputData = new ByteArrayInputStream(data);
			try {
				ZipInputStream in = new ZipInputStream(inputData);
				getImportExportBusiness().importZip(in);
				in.close();
			}
			catch (IOException e) {
				throw new UploadException(e.getMessage());
			}
			catch (DocumentException e) {
				throw new UploadException(e.getMessage());
			}
			getDao().getFileDao().remove(file.getId());
			logger.info("Import finished. " + getDaoTaskAdapter().getEnd());
		}
		catch (DaoTaskFinishedException e) {
			Queue queue = getSystemService().getQueue("import");
			queue.add(url(IMPORT_TASK_URL).param("start", 
					String.valueOf(getDaoTaskAdapter().getEnd()))
					.param("filename", filename));
			logger.info("Added new import task " + getDaoTaskAdapter().getEnd());
		}
		catch(UploadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private ImportExportBusiness getImportExportBusiness() {
		return (ImportExportBusiness) getSpringBean("importExportBusiness");
	}

	private DaoTaskAdapter getDaoTaskAdapter() {
		return (DaoTaskAdapter) getSpringBean("daoTaskAdapter");
	}
	
}
