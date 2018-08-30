package org.softuni.esports.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.esports.domain.models.view.AllJoiningsViewModel;
import org.softuni.esports.service.JoinTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/joinings")
public class JoinTournamentController extends BaseController {
    private final JoinTournamentService joinTournamentService;

    private final ModelMapper modelMapper;

    @Autowired
    public JoinTournamentController(JoinTournamentService joinTournamentService, ModelMapper modelMapper) {
        this.joinTournamentService = joinTournamentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ModelAndView allJoinings(ModelAndView modelAndView) {
        Set<AllJoiningsViewModel> allJoiningsViewModel = this
                .joinTournamentService
                .getAll()
                .stream()
                .map(x -> this.modelMapper.map(x, AllJoiningsViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("allJoinings", allJoiningsViewModel);

        return this.view("joinings-all", modelAndView);
    }
}
