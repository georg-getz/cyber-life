package org.softuni.esports.service;

import org.modelmapper.ModelMapper;
import org.softuni.esports.domain.entities.JoinTournament;
import org.softuni.esports.domain.entities.Tournament;
import org.softuni.esports.domain.entities.User;
import org.softuni.esports.domain.models.service.JoinTournamentServiceModel;
import org.softuni.esports.domain.models.service.MyTournamentsServiceModel;
import org.softuni.esports.domain.models.service.TournamentServiceModel;
import org.softuni.esports.errors.CannotJoinTwiceError;
import org.softuni.esports.errors.WrongTournamentCreateParametersError;
import org.softuni.esports.repository.JoinTournamentRepository;
import org.softuni.esports.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;

    private final JoinTournamentRepository joinTournamentRepository;

    private final UserService userService;

    private final JoinTournamentService joinTournamentService;

    private final ModelMapper modelMapper;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository, JoinTournamentRepository joinTournamentRepository, UserService userService, JoinTournamentService joinTournamentService, ModelMapper modelMapper) {
        this.tournamentRepository = tournamentRepository;
        this.joinTournamentRepository = joinTournamentRepository;
        this.userService = userService;
        this.joinTournamentService = joinTournamentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean deleteTournament(TournamentServiceModel tournamentServiceModel) {
        Tournament tournamentEntity = this.modelMapper.map(tournamentServiceModel, Tournament.class);
        Set<JoinTournamentServiceModel> joinTournamentServiceModels = this
                .joinTournamentService
                .getAll();

        System.out.println(tournamentEntity.getName());
        try {
            for (JoinTournamentServiceModel joinTournamentServiceModel : joinTournamentServiceModels) {
                if(joinTournamentServiceModel.getTournament().getId().equals(tournamentEntity.getId())){
                    JoinTournament joinTournament = this.modelMapper.map(joinTournamentServiceModel, JoinTournament.class);
                    System.out.println(joinTournament);
                    joinTournamentRepository.delete(joinTournament);
                }
            }
            this.tournamentRepository.delete(tournamentEntity);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Async
    public void createTournament(TournamentServiceModel tournamentServiceModel) {
        Tournament tournamentEntity = this.modelMapper.map(tournamentServiceModel, Tournament.class);

        if(tournamentEntity.getStartTime().compareTo(LocalDateTime.now()) < 0 ||
                tournamentEntity.getPlayerCapacity() < 2){
            return;
        }


        try {
            this.tournamentRepository.save(tournamentEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    @Transactional
    public boolean joinTournament(String tournamentId, String username) {
        Tournament tournament = this.tournamentRepository
                .findById(tournamentId)
                .orElse(null);

        User user = (User) this
                .userService
                .loadUserByUsername(username);

        if(tournament == null || user == null) {
            throw new IllegalArgumentException();
        }

        List<JoinTournament> tournamentApplications = this.joinTournamentRepository.findAllByUserId(user.getId());


        for (JoinTournament tournamentApplication : tournamentApplications) {
            if(tournamentApplication.getTournament().getName().equals(tournament.getName()))
                throw new CannotJoinTwiceError();
        }
        if(tournament.extractRemainingSpots() < 1) {
            throw new IllegalArgumentException();
        }

        JoinTournamentServiceModel joinTournamentServiceModel = new JoinTournamentServiceModel();
        joinTournamentServiceModel.setTournament(tournament);
        joinTournamentServiceModel.setUser(user);
        tournament.setPlayerCount(tournament.getPlayerCount() + 1);

        return this.joinTournamentService.createJoinRequest(joinTournamentServiceModel);
    }

    @Override
    public Set<TournamentServiceModel> getAll() {
        return this.tournamentRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, TournamentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<TournamentServiceModel> getAvailable() {
        return this.tournamentRepository
                .findAll()
                .stream()
                .filter(x -> x.getStartTime().compareTo(LocalDateTime.now()) > 0 &&
                        x.extractRemainingSpots() > 0)
                .map(x -> this.modelMapper.map(x, TournamentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<TournamentServiceModel> getUnavailable() {
        return this.tournamentRepository
                .findAll()
                .stream()
                .filter(x -> x.getStartTime().compareTo(LocalDateTime.now()) <= 0 ||
                        x.extractRemainingSpots() <= 0)
                .map(x -> this.modelMapper.map(x, TournamentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<MyTournamentsServiceModel> myTournaments(String currentUser) {
        String userId = ((User)this.userService
                .loadUserByUsername(currentUser))
                .getId();

        Set<JoinTournamentServiceModel> allJoinRequestsFromUser = this.joinTournamentService.getAllByUserId(userId);

        Set<MyTournamentsServiceModel> myEventsServiceModels = new HashSet<>();

        for (JoinTournamentServiceModel joinTournamentServiceModel : allJoinRequestsFromUser) {
            MyTournamentsServiceModel resultModel = this
                    .modelMapper.map(joinTournamentServiceModel.getTournament(), MyTournamentsServiceModel.class);

            myEventsServiceModels.add(resultModel);
        }

        return myEventsServiceModels;
    }
}
