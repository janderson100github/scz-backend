package credit.core.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class Emailer {

    public static void sendEmail(final List<String> toList,
                                 final String subject,
                                 final String msg,
                                 final String from,
                                 final String displayFromName,
                                 final String host,
                                 final String user,
                                 final String password,
                                 final String port) throws MessagingException, UnsupportedEncodingException {
        final Properties props = System.getProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        final Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from, displayFromName));
        for (final String to : toList) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }
        message.setSubject(subject);
        message.setText(msg);

        Transport.send(message);
    }

    public static void sendEmail(String to, String subject, String body)
            throws MessagingException, UnsupportedEncodingException {
        String from = "noreply";

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, "noreply"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }
}

