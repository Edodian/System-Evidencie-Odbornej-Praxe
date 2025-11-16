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
        message.setText("Dear," + fullName + "\n" +
                "Thank you for registering in SEP. Your temporary password is: " + password + " . Please, use it for your first login.\n" +
                "At your first login, it is required to change it to your own due of security purposes.\n" +
                "If you didn't register in SEP, just ignore this email\n" +
                "Best regards,\nSEP");
        mailSender.send(message);
    }
    public void sendPasswordResetCode(String to, String fullName, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password reset code – SEP");

        message.setText(
                "Dear " + fullName + ",\n\n" +
                        "A request was made to reset your SEP account password.\n\n" +
                        "Your one-time password reset code is:\n\n" +
                        "     " + resetCode + "\n\n" +
                        "Use this code to log in and set a new password.\n" +
                        "The code is valid only once.\n\n" +
                        "If you did NOT request a password reset, please ignore this message.\n\n" +
                        "Best regards,\nSEP Team"
        );

        mailSender.send(message);
    }
}

