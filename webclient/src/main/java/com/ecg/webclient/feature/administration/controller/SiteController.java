package com.ecg.webclient.feature.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller zum Behandeln von Login Requests.
 * 
 * @author arndtmar
 *
 */
@Controller
public class SiteController
{
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/login/error/{errorCause}", method = RequestMethod.GET)
    public String loginError(Model model, @PathVariable("errorCause") String errorCause)
    {
        model.addAttribute("loginFailed", true);
        model.addAttribute("errorCause", errorCause);
        return "login";
    }
}
