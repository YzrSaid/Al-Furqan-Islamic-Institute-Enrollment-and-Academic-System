package com.example.testingLogIn.PasswordResetPackage;

import com.example.testingLogIn.ModelDTO.UserDTO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private final Properties prop;
    private final Authenticator authenticator;
    private final Session session;

    public EmailService(){
        String to = "eh202200596@wmsu.edu.ph";
        String from = "magnoclifford42@gmail.com";
        String username = "magnoclifford42@gmail.com";
        String password = "ggiv ohop xrwd aehy";
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        authenticator = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        };

        session = Session.getInstance(prop,authenticator);
    }

    public void sendEmail(String receiver){
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress("magnoclifford42@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiver));
            msg.setSubject("Testing email");
            msg.setText("Testing");
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetEmail(String recipientEmail, String token) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("magnoclifford42@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset Request");
            String resetLink = "localhost:8081/reset-password/" + token;

            // HTML email content
            String htmlContent = "<h3>Password Reset</h3>"
                    + "<p>We received a request to reset your password. Click the link below:</p>"
                    + "<p>" + resetLink + "</p>"
                    + "<p>This link will expire in 24 hours.</p>"
                    + "<p>If you didn't request this, please ignore this email.</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
