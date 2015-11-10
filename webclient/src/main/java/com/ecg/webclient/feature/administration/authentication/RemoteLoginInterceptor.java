package com.ecg.webclient.feature.administration.authentication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.cookie.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ecg.webclient.feature.administration.service.RemoteSystemService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Dieser Interceptor wird ausgeführt nach erfolgreichem Login um die Cookies der Fremdsysteme zu setzen. Wird
 * beim Pfad "/main" getriggert.
 * 
 * @author arndtmar
 *
 */
@Component
public class RemoteLoginInterceptor extends HandlerInterceptorAdapter
{
    static final Logger logger = LogManager.getLogger(RemoteLoginInterceptor.class.getName());

    @Autowired
    RemoteSystemService remoteSystemService;
    @Autowired
    UserService         userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception
    {
        // Anmeldung an Fremdsystemen versuchen
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDto user = userService.getUserByLogin(auth.getName());

            List<Cookie> cookies = remoteSystemService.doRemoteLogin(user.getId());

            for (Cookie remoteCookie : cookies)
            {
                javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(remoteCookie.getName(),
                        remoteCookie.getValue());
                cookie.setHttpOnly(true);
                cookie.setPath(remoteCookie.getPath());

                response.addCookie(cookie);
            }

        }
        catch (Exception exc)
        {
            logger.error("remote login failed.", exc);
        }
    }
}
