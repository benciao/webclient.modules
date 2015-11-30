package com.ecg.webclient.feature.administration.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.service.EnvironmentService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Dieser Handler entscheided, ob der Benutzer nach erfolgreichem Login sein Passwort ändern muss oder nicht.
 * 
 * @author arndtmar
 */
@Component("webClientAuthenticationSuccessHandler")
public class WebClientAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    UserService        userService;
    @Autowired
    EnvironmentService environmentService;

    RedirectStrategy   redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication auth) throws IOException, ServletException
    {
        String login = auth.getName();
        UserDto user = userService.getUserByLogin(login);
        boolean isPasswordExpired = isPasswordExpired(user.getPasswortChangedTimeStamp());

        // Passwort muss geändert werden, wenn am Nutzer die Eigenschaft gesetzt ist oder das Passwort
        // abgelaufen ist
        if (user.isChangePasswordOnNextLogin() || isPasswordExpired)
        {
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            String password = auth.getCredentials().toString();
            auth.getAuthorities().forEach(e -> grantedAuths.add(e));

            grantedAuths.add(new DbGrantedAuthoritiy("ROLE_SEC_FORCE_CHANGE_PASSWORD"));

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(login, password, grantedAuths));
            redirectStrategy.sendRedirect(request, response, "/changepw");
        }
        else
        {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }

    private boolean isPasswordExpired(Date passwortChangedTimeStamp)
    {
        int passwordChangeIntervalInDays = environmentService.getEnvironment().getPasswordChangeInterval();

        Calendar passwortChangedCal = Calendar.getInstance();
        passwortChangedCal.setTime(passwortChangedTimeStamp);
        passwortChangedCal.set(Calendar.HOUR_OF_DAY, 0);
        passwortChangedCal.set(Calendar.MINUTE, 0);
        passwortChangedCal.set(Calendar.SECOND, 0);
        passwortChangedCal.set(Calendar.MILLISECOND, 0);

        passwortChangedCal.add(Calendar.DAY_OF_MONTH, passwordChangeIntervalInDays);

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        return passwortChangedCal.getTimeInMillis() <= todayCal.getTimeInMillis();
    }
}
