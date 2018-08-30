package org.softuni.esports.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "tournament_applications")
public class JoinTournament extends BaseEntity {
    private Tournament tournament;

    private User user;

    public JoinTournament() {
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_id", nullable = false)
    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
