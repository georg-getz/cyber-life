package org.softuni.esports.domain.models.service;

import org.softuni.esports.domain.entities.Tournament;
import org.softuni.esports.domain.entities.User;

public class JoinTournamentServiceModel {
    private String id;

    private Tournament tournament;

    private User user;

    public JoinTournamentServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
