package sk.ukf.sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Hello from Spring + Mailpit!");
        message.setText("This is a test email sent through Mailpit.");

        mailSender.send(message);
    }

    public void sendTemporaryPassword(String to, String fullName, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Temporary password for SEP");
        message.setText("Hello," + fullName + "\n" +
                "Thank you for registering in SEP. Your temporary password is: " + password + " . Please, use it for your first login.\n" +
                "After that you will be kindly asked to change it to your own due of security purposes.\n" +
                "If you did't registered in SEP, just ignore this email\n" +
                "Best regards,\nSEP");
    }
}

