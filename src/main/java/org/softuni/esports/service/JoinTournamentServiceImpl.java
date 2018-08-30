package org.softuni.esports.service;

import org.modelmapper.ModelMapper;
import org.softuni.esports.domain.entities.JoinTournament;
import org.softuni.esports.domain.models.service.JoinTournamentServiceModel;
import org.softuni.esports.repository.JoinTournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JoinTournamentServiceImpl implements JoinTournamentService {
    private final JoinTournamentRepository joinTournamentRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public JoinTournamentServiceImpl(JoinTournamentRepository joinTournamentRepository, ModelMapper modelMapper) {
        this.joinTournamentRepository = joinTournamentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean createJoinRequest(JoinTournamentServiceModel joinTournamentServiceModel) {
        JoinTournament joinTournamentEntity = this.modelMapper.map(joinTournamentServiceModel,  JoinTournament.class);

        try {
            this.joinTournamentRepository.save(joinTournamentEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Set<JoinTournamentServiceModel> getAll() {
        return this.joinTournamentRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, JoinTournamentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<JoinTournamentServiceModel> getAllByUserId(String userId) {
        return this.joinTournamentRepository
                .findAllByUserId(userId)
                .stream()
                .map(x -> this.modelMapper.map(x, JoinTournamentServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}
