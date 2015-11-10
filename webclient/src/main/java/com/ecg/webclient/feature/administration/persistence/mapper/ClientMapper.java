package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Client;
import com.ecg.webclient.feature.administration.persistence.repo.ClientRepository;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf einen detachten Mandanten oder
 * umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class ClientMapper
{
    @Autowired
    ClientRepository clientRepo;

    /**
     * Wandelt einen attachten Mandanten in einen detachten um.
     * 
     * @param client
     *            attachten Mandant
     * @return Detacheter Mandant
     */
    public ClientDto mapToDto(Client client)
    {
        if (client != null)
        {
            ClientDto dto = new ClientDto();
            dto.setColor(client.getColor());
            dto.setDescription(client.getDescription());
            dto.setName(client.getName());
            dto.setEnabled(client.isEnabled());
            dto.setDelete(false);
            dto.setId(client.getId());

            return dto;
        }
        else
        {
            return null;
        }
    }

    /**
     * Wandelt eine Liste von attachten Mandanten in eine Liste von detachten Mandanten um.
     * 
     * @param clients
     *            Liste von attachten Mandanten
     * @return Liste von detachten Mandanten
     */
    public List<ClientDto> mapToDtos(List<Client> clients)
    {
        List<ClientDto> result = new AutoPopulatingList<ClientDto>(ClientDto.class);

        clients.forEach(e -> result.add(mapToDto(e)));

        return result;
    }

    /**
     * Wandelt eine Liste von detachten Mandanten in eine Liste von attachten Mandanten um.
     * 
     * @param dtos
     *            Liste von detachten Mandanten
     * @return Liste von attachten Mandanten
     */
    public List<Client> mapToEntities(List<ClientDto> dtos)
    {
        List<Client> result = new ArrayList<Client>();

        dtos.forEach(e -> result.add(mapToEntity(e)));

        return result;
    }

    /**
     * Wandelt einen detachten Mandanten in einen attachter um.
     * 
     * @param dto
     *            Detachter Mandant
     * @return attachter Mandant
     */
    public Client mapToEntity(ClientDto dto)
    {
        if (dto != null)
        {
            Client client = new Client();
            client.setId(dto.getId());
            client.setColor(dto.getColor());
            client.setDescription(dto.getDescription());
            client.setName(dto.getName());
            client.setEnabled(dto.isEnabled());

            Client persistentClient = clientRepo.findOne(client.getId());
            if (persistentClient != null)
            {
                return persistentClient.bind(client);
            }

            return client;
        }
        return null;
    }
}
