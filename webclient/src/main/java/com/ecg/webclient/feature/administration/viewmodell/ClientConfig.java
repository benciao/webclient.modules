package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class ClientConfig
{
    @Valid
    private List<ClientDto> clients;

    public List<ClientDto> getClients()
    {
        if (clients == null)
        {
            clients = new AutoPopulatingList<ClientDto>(ClientDto.class);
        }
        return clients;
    }

    public void removeDeleted()
    {
        List<ClientDto> clientsToRemove = new ArrayList<ClientDto>();
        for (ClientDto client : clients)
        {
            if (client.isDelete())
            {
                clientsToRemove.add(client);
            }
        }
        clients.removeAll(clientsToRemove);
    }

    public void setClients(List<ClientDto> clients)
    {
        this.clients = clients;
    }
}
