package com.ecg.webclient.feature.administration.viewmodell.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.service.RemoteSystemService;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemConfig;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ {@link RemoteSystemDto}.
 * 
 * @author arndtmar
 */
@Component
public class RemoteSystemDtoValidator implements Validator
{
    @Autowired
    RemoteSystemService remoteSystemService;

    @Override
    public boolean supports(Class<?> clazz)
    {
        return RemoteSystemConfig.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        List<RemoteSystemDto> remoteSystems = ((RemoteSystemConfig) object).getRemoteSystems();

        int counter = 0;
        for (RemoteSystemDto remoteSystem : remoteSystems)
        {
            if (remoteSystem.getId() == -1)
            {
                RemoteSystemDto result = remoteSystemService.getRemoteSystemByName(remoteSystem.getName());

                if (result != null)
                {
                    errors.rejectValue("remoteSystems[" + counter + "].name",
                            "remotesystem.rejected.duplicated.name");
                }
            }

            if (remoteSystem.getName().isEmpty() || remoteSystem.getName().length() < 4
                    || remoteSystem.getName().length() > 100)
            {
                errors.rejectValue("remoteSystems[" + counter + "].name", "remotesystem.rejected.size.name");
            }
            if (remoteSystem.getDescription().isEmpty() || remoteSystem.getDescription().length() < 10
                    || remoteSystem.getDescription().length() > 100)
            {
                errors.rejectValue("remoteSystems[" + counter + "].description",
                        "remotesystem.rejected.size.description");
            }
            if (remoteSystem.getLoginUrl().isEmpty() || remoteSystem.getLoginUrl().length() < 1)
            {
                errors.rejectValue("remoteSystems[" + counter + "].loginUrl",
                        "remotesystem.rejected.size.loginUrl");
            }
            if (remoteSystem.getLoginParameter().isEmpty() || remoteSystem.getLoginParameter().length() < 1)
            {
                errors.rejectValue("remoteSystems[" + counter + "].loginParameter",
                        "remotesystem.rejected.size.loginParameter");
            }
            if (remoteSystem.getPasswordParameter().isEmpty()
                    || remoteSystem.getPasswordParameter().length() < 1)
            {
                errors.rejectValue("remoteSystems[" + counter + "].passwordParameter",
                        "remotesystem.rejected.size.passwordParameter");
            }

            counter++;
        }
    }

}
