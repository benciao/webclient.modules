package com.ecg.webclient.feature.administration.viewmodell.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.viewmodell.EnvironmentDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ {@link EnvironmentDto}.
 * 
 * @author arndtmar
 */
@Component
public class EnvironmentDtoValidator implements Validator
{

    @Override
    public boolean supports(Class<?> clazz)
    {
        return EnvironmentDto.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        EnvironmentDto env = (EnvironmentDto) object;

        if (env.getPasswordChangeInterval() == null || env.getPasswordChangeInterval() < 2
                || env.getPasswordChangeInterval() > 100)
        {
            errors.rejectValue("passwordChangeInterval", "environment.rejected.size.pwChangeInterval");
        }
        if (env.getAllowedLoginAttempts() == null || env.getAllowedLoginAttempts() < 2
                || env.getAllowedLoginAttempts() > 10)
        {
            errors.rejectValue("allowedLoginAttempts", "environment.rejected.size.allowedLoginAttempts");
        }
    }

}
