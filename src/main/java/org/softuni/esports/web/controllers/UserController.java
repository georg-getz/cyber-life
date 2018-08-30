package org.softuni.esports.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.esports.domain.models.binding.PromoteDemoteUserBindingModel;
import org.softuni.esports.domain.models.binding.UserRegisterBindingModel;
import org.softuni.esports.domain.models.service.UserServiceModel;
import org.softuni.esports.service.MailService;
import org.softuni.esports.service.RecaptchaService;
import org.softuni.esports.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    private final RecaptchaService recaptchaService;

    private final ModelMapper modelMapper;

    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, RecaptchaService recaptchaService, ModelMapper modelMapper, MailService mailService) {
        this.userService = userService;
        this.recaptchaService = recaptchaService;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return this.view("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return this.view("register");
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel, @RequestParam(name = "g-recaptcha-response") String gRecaptchaResponse, HttpServletRequest request) {
        if(!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword())) {
            return this.view("register");
        }

        if(this.recaptchaService
                .verifyRecaptcha(request.getRemoteAddr()
                        , gRecaptchaResponse) == null) {
            return this.view("register");
        }
        if(userRegisterBindingModel.getEmail().equals("") ||
                userRegisterBindingModel.getPassword().equals("") ||
                userRegisterBindingModel.getUsername().equals("") ||
                userRegisterBindingModel.getConfirmPassword().equals(" "))
            return this.view("register");

        this.userService
                .createUser(this.modelMapper
                        .map(userRegisterBindingModel, UserServiceModel.class));

        this.mailService.sendRegistrationSuccessMessage(userRegisterBindingModel.getEmail(), userRegisterBindingModel.getUsername());

        return this.redirect("/login");
    }
    @GetMapping("/users")
    public ModelAndView allUsers(ModelAndView modelAndView){
        Set<UserServiceModel> allUsers = this
                .userService
                .getAll()
                .stream()
                .map(x -> this.modelMapper
                        .map(x, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("allUsers", allUsers);
        return this.view("users-all", modelAndView);
    }

    @PostMapping("/users/promote")
    public ModelAndView promoteUser(@ModelAttribute PromoteDemoteUserBindingModel promoteDemoteUserBindingModel){
        boolean result = this
                .userService
                .promoteUser(promoteDemoteUserBindingModel.getUserId());

        if(!result){
            throw new IllegalArgumentException();
        }
        return this.redirect("/users");
    }

    @PostMapping("/users/demote")
    public ModelAndView demoteUser(@ModelAttribute PromoteDemoteUserBindingModel promoteDemoteUserBindingModel){
        boolean result = this
                .userService
                .demoteUser(promoteDemoteUserBindingModel.getUserId());

        if(!result){
            throw new IllegalArgumentException();
        }
        return this.redirect("/users");
    }

}
