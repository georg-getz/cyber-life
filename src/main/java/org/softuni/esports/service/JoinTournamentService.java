package org.softuni.esports.service;

import org.softuni.esports.domain.models.service.JoinTournamentServiceModel;

import java.util.Set;

public interface JoinTournamentService {
    boolean createJoinRequest(JoinTournamentServiceModel joinTournamentServiceModel);

    Set<JoinTournamentServiceModel> getAll();

    Set<JoinTournamentServiceModel> getAllByUserId(String userId);
}
