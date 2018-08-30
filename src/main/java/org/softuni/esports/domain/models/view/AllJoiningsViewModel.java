package org.softuni.esports.domain.models.view;

public class AllJoiningsViewModel {
    private String tournamentName;

    private String userUsername;

    public AllJoiningsViewModel() {
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
}
