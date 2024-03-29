package com.wrapper.app.infrastructure.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

@Component
public class EmailSender {

    @Value("${email.sender.email}")
    private String senderEmail;
    @Value("${email.sender.pass}")
    private String senderPassword;

    public void sendEmail(List<String> recipientEmails, String subject, String body, String attachmentFilePath) {
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com"); // Update with your SMTP server
        props.put("mail.smtp.port", "587"); // Update with the appropriate port (587 for TLS)

        // Create the session with authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            InternetAddress[] recipientAddresses = convertToInternetAddresses(recipientEmails);
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject(subject);

            // Create the email body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create the attachment
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(attachmentFilePath);

            // Create a multipart message and add the parts to it
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            // Set the message content
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InternetAddress[] convertToInternetAddresses(List<String> recipientEmails) {
        return recipientEmails.stream()
                .map(email -> {
                    try {
                        return new InternetAddress(email);
                    } catch (javax.mail.internet.AddressException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .toArray(InternetAddress[]::new);
    }
}
