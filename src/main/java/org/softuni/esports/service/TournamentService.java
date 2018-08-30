package org.softuni.esports.service;

import org.softuni.esports.domain.models.service.MyTournamentsServiceModel;
import org.softuni.esports.domain.models.service.TournamentServiceModel;

import java.util.Set;

public interface TournamentService {
    boolean deleteTournament(TournamentServiceModel tournamentServiceModel);

    void createTournament(TournamentServiceModel tournamentServiceModel);

    boolean joinTournament(String tournamentId, String username);

    Set<TournamentServiceModel> getAll();

    Set<TournamentServiceModel> getAvailable();

    Set<TournamentServiceModel> getUnavailable();

    Set<MyTournamentsServiceModel> myTournaments(String currentUser);
}
