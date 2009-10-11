package org.vosao.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

import org.vosao.servlet.FileItem;
import org.vosao.servlet.FolderUtil;
import org.vosao.servlet.MimeType;

public class EmailUtil {

	/**
	 * Send email with html content.
	 * @param htmlBody
	 * @param subject
	 * @param fromAddress
	 * @param fromText
	 * @param toAddress
	 * @return null if OK or error message.
	 */
	public static String sendEmail(final String htmlBody, final String subject, 
			final String fromAddress, final String fromText, 
			final String toAddress) {
		return sendEmail(htmlBody, subject, fromAddress, fromText, toAddress, 
				new ArrayList<FileItem>());
	}

	/**
	 * Send email with html content and attachments.
	 * @param htmlBody
	 * @param subject
	 * @param fromAddress
	 * @param fromText
	 * @param toAddress
	 * @return null if OK or error message.
	 */
	public static String sendEmail(final String htmlBody, final String subject, 
			final String fromAddress, final String fromText, 
			final String toAddress, final List<FileItem> files) {

		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
        	Multipart mp = new MimeMultipart();
        	MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            //htmlPart.setHeader(name, value)
            mp.addBodyPart(htmlPart);
            for (FileItem item : files) {
            	MimeBodyPart attachment = new MimeBodyPart();
                attachment.setFileName(item.getFilename());
                String mimeType = MimeType.getContentTypeByExt(
                		FolderUtil.getFileExt(item.getFilename()));
                attachment.setContent(item.getData(), mimeType);
                mp.addBodyPart(attachment);
            }
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromAddress, fromText));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(toAddress, toAddress));
            msg.setSubject(subject, "UTF-8");
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
