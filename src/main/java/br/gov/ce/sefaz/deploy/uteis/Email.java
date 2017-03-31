package br.gov.ce.sefaz.deploy.uteis;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class Email {
	
	@Resource(name = "java:jboss/mail/sefaz")
    private Session mailSession;
	
	public void send(String remetente, String subject, String body) {
		try {
			MimeMessage m = new MimeMessage(mailSession);
			Address from = new InternetAddress("web@sefaz.ce.gov.br");
			Address[] to = new InternetAddress[] { new InternetAddress(remetente) };

			m.setFrom(from);
			m.setRecipients(Message.RecipientType.TO, to);
			m.setSubject(subject);
			m.setSentDate(new java.util.Date());
			m.setContent(body, "text/plain");
			Transport.send(m);
			
			
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
		}
	}
}
