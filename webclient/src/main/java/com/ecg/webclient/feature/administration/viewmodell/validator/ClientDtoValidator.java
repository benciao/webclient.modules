package com.ecg.webclient.feature.administration.viewmodell.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.viewmodell.ClientConfig;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ {@link ClientDto}.
 * 
 * @author arndtmar
 */
@Component
public class ClientDtoValidator implements Validator
{
    @Autowired
    ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz)
    {
        return ClientConfig.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors)
    {
        List<ClientDto> clients = ((ClientConfig) object).getClients();

        int counter = 0;
        for (ClientDto client : clients)
        {
            if (client.getId() == -1)
            {
                ClientDto result = clientService.getClientByName(client.getName());

                if (result != null)
                {
                    errors.rejectValue("clients[" + counter + "].name", "client.rejected.duplicated.name");
                }
            }

            if (client.getName().isEmpty() || client.getName().length() < 2 || client.getName().length() > 20)
            {
                errors.rejectValue("clients[" + counter + "].name", "client.rejected.size.name");
            }
            if (client.getDescription().isEmpty() || client.getDescription().length() < 10
                    || client.getDescription().length() > 50)
            {
                errors.rejectValue("clients[" + counter + "].description", "client.rejected.size.description");
            }

            counter++;
        }
    }
}
