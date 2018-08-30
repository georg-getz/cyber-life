package org.softuni.esports.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.ModelAndView;

abstract class BaseController {
    protected ModelAndView view(String view, ModelAndView modelAndView) {
        modelAndView.setViewName(view);

        return modelAndView;
    }

    protected String getPrincipalAuthority(Authentication authentication) {
        GrantedAuthority principalAuthority = authentication
                .getAuthorities()
                .stream()
                .findFirst()
                .orElse(null);

        return principalAuthority != null
                ? principalAuthority.getAuthority()
                : null;
    }

    protected ModelAndView view(String view) {
        return this.view(view, new ModelAndView());
    }

    protected ModelAndView view(String view, String name, Object model){
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject(name, model);
        return modelAndView;
    }

    protected ModelAndView redirect(String route) {
        return this.view("redirect:" + route);
    }
}