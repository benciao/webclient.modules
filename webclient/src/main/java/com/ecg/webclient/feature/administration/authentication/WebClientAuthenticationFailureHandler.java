package com.ecg.webclient.feature.administration.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("webClientAuthenticationFailureHandler")
public class WebClientAuthenticationFailureHandler implements AuthenticationFailureHandler
{
	RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException
	{
		if (exception instanceof BadCredentialsException)
		{
            redirectStrategy.sendRedirect(request, response, "/login/error/loginFailureBc");
		}
        else if (exception instanceof DisabledException)
        {
            redirectStrategy.sendRedirect(request, response, "/login/error/loginFailureD");
        }
        else if (exception instanceof LockedException)
        {
            redirectStrategy.sendRedirect(request, response, "/login/error/loginFailureL");
        }
        else if (exception instanceof InsufficientAuthenticationException)
        {
            redirectStrategy.sendRedirect(request, response, "/login/error/loginFailureLdapDisabled");
        }
        else
        {
            redirectStrategy.sendRedirect(request, response, "/error");
        }
	}

}
