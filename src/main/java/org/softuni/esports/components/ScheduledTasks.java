package org.softuni.esports.components;

import org.softuni.esports.domain.models.service.JoinTournamentServiceModel;
import org.softuni.esports.domain.models.service.TournamentServiceModel;
import org.softuni.esports.service.JoinTournamentService;
import org.softuni.esports.service.MailService;
import org.softuni.esports.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class ScheduledTasks {

    private final JoinTournamentService joinTournamentService;

    private final MailService mailService;

    private final TournamentService tournamentService;

    @Autowired
    public ScheduledTasks(JoinTournamentService joinTournamentService, MailService mailService, TournamentService tournamentService) {
        this.joinTournamentService = joinTournamentService;
        this.mailService = mailService;
        this.tournamentService = tournamentService;
    }

    @Scheduled(fixedRate = 3600000)
    public void remindAboutUpcomingTournament(){
        Set<JoinTournamentServiceModel> joinTournamentServiceModels = this
                .joinTournamentService
                .getAll();

        for (JoinTournamentServiceModel tournamentServiceModel : joinTournamentServiceModels) {
            if(tournamentServiceModel.getTournament().getStartTime()
                    .compareTo(LocalDateTime.now().plusHours(1)) < 0){
                this.mailService.sendTournamentReminder(tournamentServiceModel.getUser().getEmail(),
                        tournamentServiceModel.getUser().getUsername(),
                        tournamentServiceModel.getTournament().getName());
            }
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void deletePassedNotFilledTournaments(){
        Set<TournamentServiceModel> tournamentServiceModels = this
                .tournamentService
                .getAll();

        for (TournamentServiceModel tournament : tournamentServiceModels) {
            if(tournament.getStartTime().compareTo(LocalDateTime.now()) < 0 &&
                    tournament.getPlayerCount() < tournament.getPlayerCapacity())
                this.tournamentService.deleteTournament(tournament);
        }
    }
}
