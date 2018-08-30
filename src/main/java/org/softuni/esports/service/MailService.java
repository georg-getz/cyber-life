package org.softuni.esports.service;

import java.time.LocalDateTime;

public interface MailService {
    void sendRegistrationSuccessMessage(String email, String username);

    void sendTournamentReminder(String email, String username, String tournamentName);
}
