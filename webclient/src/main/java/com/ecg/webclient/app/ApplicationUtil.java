package com.ecg.webclient.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.service.EnvironmentService;

@Scope("session")
@Component
public class ApplicationUtil
{
    static final String PROPERTY_NAME_IS_LOGIN_AS_USER_ALLOWED = "sec.is.login.as.other.user.allowed";

    boolean             isMenuMinimized;
    EnvironmentService  environmentService;
    Environment         env;

    @Autowired
    public ApplicationUtil(EnvironmentService environmentService, Environment env)
    {
        this.isMenuMinimized = false;
        this.environmentService = environmentService;
        this.env = env;
    }

    public String getSystemIdentifier()
    {
        String identifier = environmentService.getEnvironment().getSystemIdentifier();
        if (identifier != null && identifier.length() == 0)
        {
            return null;
        }
        else
        {
            return identifier;
        }
    }

    public boolean isLoginAsUserAllowed()
    {
        String value = env.getRequiredProperty(PROPERTY_NAME_IS_LOGIN_AS_USER_ALLOWED);

        return Boolean.valueOf(value);
    }

    public boolean isMenuMinimized()
    {
        return isMenuMinimized;
    }

    public void setMenuMinimized(boolean isMenuMinimized)
    {
        this.isMenuMinimized = isMenuMinimized;
    }
}
