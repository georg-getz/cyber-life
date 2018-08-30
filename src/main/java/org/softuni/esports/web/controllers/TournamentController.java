package org.softuni.esports.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.esports.domain.models.binding.JoinTournamentBindingModel;
import org.softuni.esports.domain.models.binding.TournamentCreateBindingModel;
import org.softuni.esports.domain.models.service.TournamentServiceModel;
import org.softuni.esports.domain.models.view.AllTournamentsViewModel;
import org.softuni.esports.domain.models.view.MyTournamentsViewModel;
import org.softuni.esports.errors.CannotJoinTwiceError;
import org.softuni.esports.service.JoinTournamentService;
import org.softuni.esports.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tournaments")
public class TournamentController extends BaseController {
    private final TournamentService tournamentService;

    private final JoinTournamentService joinTournamentService;

    private final ModelMapper modelMapper;

    @Autowired
    public TournamentController(TournamentService tournamentService, JoinTournamentService joinTournamentService, ModelMapper modelMapper) {
        this.tournamentService = tournamentService;
        this.joinTournamentService = joinTournamentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ModelAndView allTournaments(ModelAndView modelAndView) {
        return this.view("tournaments-all");
    }


    @GetMapping(value = "/api/all", produces = "application/json")
    @ResponseBody
    public Set<AllTournamentsViewModel> apiAllTournaments() {

        return this
                .tournamentService
                .getAll()
                .stream()
                .map(x ->  {
                    AllTournamentsViewModel viewModel = this.modelMapper.map(x, AllTournamentsViewModel.class);

                    viewModel.setPlayerCount(x.getPlayerCount());
                    viewModel.setAvailable(
                            viewModel.getStartTime().compareTo(LocalDateTime.now()) > 0 && viewModel.getPlayerCount() < viewModel.getPlayerCapacity()
                    );

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @GetMapping(value = "/api/available", produces = "application/json")
    @ResponseBody
    public Set<AllTournamentsViewModel> apiAvailableTournaments() {

        return this
                .tournamentService
                .getAvailable()
                .stream()
                .map(x ->  {
                    AllTournamentsViewModel viewModel = this.modelMapper.map(x, AllTournamentsViewModel.class);

                    viewModel.setAvailable(true);
                    viewModel.setPlayerCount(x.getPlayerCount());

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }
    @GetMapping(value = "/api/unavailable", produces = "application/json")
    @ResponseBody
    public Set<AllTournamentsViewModel> apiUnavailableTournaments() {

        return this
                .tournamentService
                .getUnavailable()
                .stream()
                .map(x ->  {
                    AllTournamentsViewModel viewModel = this.modelMapper.map(x, AllTournamentsViewModel.class);

                    viewModel.setAvailable(false);
                    viewModel.setPlayerCount(x.getPlayerCount());

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @GetMapping("/my")
    public ModelAndView myTournaments(Principal principal, ModelAndView modelAndView) {
        Set<MyTournamentsViewModel> myTournamentsViewModel = this
                .tournamentService
                .myTournaments(principal.getName())
                .stream()
                .map(x -> this.modelMapper.map(x, MyTournamentsViewModel.class))
                .collect(Collectors.toUnmodifiableSet());


        modelAndView.addObject("myTournaments", myTournamentsViewModel);

        return this.view("tournaments-my", modelAndView);
    }

    @GetMapping("/create")
    public ModelAndView createTournament() {
        return this.view("tournaments-create");
    }

    @PostMapping("/create")
    public ModelAndView createTournamentConfirm(@ModelAttribute TournamentCreateBindingModel tournamentCreateBindingModel) {
        this.tournamentService
                .createTournament(this.modelMapper.map(tournamentCreateBindingModel, TournamentServiceModel.class));

        return this.redirect("all");
    }

    @PostMapping("/join")
    public ModelAndView join(@ModelAttribute JoinTournamentBindingModel joinTournamentBindingModel, Principal principal, ModelAndView modelAndView) {
        boolean result = this.tournamentService
                .joinTournament(joinTournamentBindingModel.getTournamentId(), principal.getName());

        if(!result){
            throw new IllegalArgumentException();
        }

        return this.redirect("all");
    }
}
