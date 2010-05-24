package org.vosao.utils

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

object EmailUtil {

	val logger = LogFactory.getLog(EmailUtil.getClass)
	
	/**
	 * Send email with html content.
	 * @param htmlBody
	 * @param subject
	 * @param fromAddress
	 * @param fromText
	 * @param toAddress
	 * @return null if OK or error message.
	 */
	def sendEmail(htmlBody: String, subject: String, fromAddress: String, 
			fromText: String, toAddress: String): String = {
		sendEmail(htmlBody, subject, fromAddress, fromText, toAddress, Nil);
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
	def sendEmail(htmlBody: String, subject: String, fromAddress: String, 
			fromText: String, toAddress: String, files: List[FileItem]): String = {
		val props = new Properties()
        val session = Session.getDefaultInstance(props, null)
        try {
        	val mp = new MimeMultipart()
        	val htmlPart = new MimeBodyPart()
            htmlPart.setContent(htmlBody, "text/html")
            mp.addBodyPart(htmlPart)
            for (item <- files) {
            	val attachment = new MimeBodyPart()
                attachment.setFileName(item.getFilename())
                val mimeType = MimeType.getContentTypeByExt(
                		FolderUtil.getFileExt(item.getFilename()))
                val ds = new ByteArrayDataSource(item.getData(), mimeType)
                attachment.setDataHandler(new DataHandler(ds))
                mp.addBodyPart(attachment)
            }
            val msg = new MimeMessage(session)
            msg.setFrom(new InternetAddress(fromAddress, fromText))
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(toAddress, toAddress))
            msg.setSubject(subject, "UTF-8")
            msg.setContent(mp)
            Transport.send(msg)
            null;
        } catch {
        	case e: AddressException => e.getMessage()
        	case e: MessagingException => e.getMessage()
        	case e: UnsupportedEncodingException => e.getMessage()
		}		
	}
	
	
}
