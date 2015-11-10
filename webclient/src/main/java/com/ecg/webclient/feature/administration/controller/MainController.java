package com.ecg.webclient.feature.administration.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.app.ApplicationUtil;
import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Controller zur Bearbeitung von Requests der Startseite des Features Administration.
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
public class MainController
{
    static final Logger         logger = LogManager.getLogger(MainController.class.getName());

    @Autowired
    private ClientService       clientService;
    @Autowired
    UserService                 userService;
    @Autowired
    private AuthenticationUtil  authUtil;
    @Autowired
    private ApplicationUtil     util;

    public MainController()
    {}

    @RequestMapping(value = "/changeClient", method = RequestMethod.POST)
    public String changeClient(@ModelAttribute("selectedClient") Long selectedClient)
    {
        authUtil.setSelectedClientWithNewAuthority(clientService.getClient(selectedClient));
        authUtil.setSelectedFeature(null);
        return "/main";
    }

    @RequestMapping(value = "/main/tooglemenue/true", method = RequestMethod.GET)
    public void hideMenu()
    {
        util.setMenuMinimized(true);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginOk(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        authUtil.setSelectedFeature(null);
        UserDto user = userService.getUserByLogin(auth.getName());
        ClientDto defaultClient = userService.getDefaultClientForUser(user);
        authUtil.setSelectedClient(defaultClient);

        return "/main";
    }

    @RequestMapping(value = "/main/tooglemenue/false", method = RequestMethod.GET)
    public void showMenu()
    {
        util.setMenuMinimized(false);
    }

    @RequestMapping(value = "/switchLocale", method = RequestMethod.GET)
    public String switchLocale(HttpServletRequest request)
    {
        if (authUtil.getSessionLocaleResolver().resolveLocale(request).equals(Locale.ENGLISH))
        {
            authUtil.getSessionLocaleResolver().setDefaultLocale(Locale.GERMAN);
        }
        else
        {
            authUtil.getSessionLocaleResolver().setDefaultLocale(Locale.ENGLISH);
        }

        return "/main";
    }
}
