package org.softuni.esports.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "tournaments")
public class Tournament extends BaseEntity {
    private String name;

    private String gameName;

    private int playerCapacity;

    private int playerCount;

    private LocalDateTime startTime;

    public Tournament() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "game_name", nullable = false)
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Column(name = "player_capacity", nullable = false)
    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public void setPlayerCapacity(int playerCapacity) {
        this.playerCapacity = playerCapacity;
    }

    @Column(name = "players_count", nullable = false)
    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int extractRemainingSpots(){
        return this.playerCapacity - this.playerCount;
    }
}
