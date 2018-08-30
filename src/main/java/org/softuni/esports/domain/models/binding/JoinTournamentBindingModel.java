package org.softuni.esports.domain.models.binding;

public class JoinTournamentBindingModel {
    private String tournamentId;

    private int playerCapacity;

    public JoinTournamentBindingModel() {
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public void setPlayerCapacity(int playerCapacity) {
        this.playerCapacity = playerCapacity;
    }
}
