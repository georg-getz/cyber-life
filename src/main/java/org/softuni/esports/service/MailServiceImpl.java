package org.softuni.esports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailServiceImpl implements MailService {
    private static final String REGISTRATION_MAIL_SUBJECT =
            "Cyber Life - [Registration]";

    private static final String REGISTRATION_SUCCESS_MESSAGE
            = "Welcome, %s! You have successfully joined Cyber Life.";

    private static final String TOURNAMENT_REMINDER_SUBJECT =
            "Tournament Reminder";

    private static final String TOURNAMENT_REMINDER_MESSAGE =
            "%s, don't forget \"%s\" starts in less than an hour!";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationSuccessMessage(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(REGISTRATION_MAIL_SUBJECT);
        message.setText(String.format(REGISTRATION_SUCCESS_MESSAGE, username));

        message.setFrom("info@cyberlife.io");
        message.setTo(email);

        this.javaMailSender.send(message);
    }

    @Override
    public void sendTournamentReminder(String email, String username, String tournamentName) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(TOURNAMENT_REMINDER_SUBJECT);
        message.setText(String.format(TOURNAMENT_REMINDER_MESSAGE, username, tournamentName));

        message.setFrom("info@cyberlife.io");
        message.setTo(email);

        this.javaMailSender.send(message);
    }
}
