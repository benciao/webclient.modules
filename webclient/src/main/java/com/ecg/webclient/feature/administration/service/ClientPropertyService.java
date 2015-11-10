package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.mapper.ClientPropertyMapper;
import com.ecg.webclient.feature.administration.persistence.modell.ClientProperty;
import com.ecg.webclient.feature.administration.persistence.repo.ClientPropertyRepository;
import com.ecg.webclient.feature.administration.viewmodell.ClientPropertyDto;

/**
 * Service zum Bearbeiten von Mandanteneigenschaften.
 * 
 * @author arndtmar
 */
@Component
public class ClientPropertyService
{
    static final Logger      logger = LogManager.getLogger(ClientService.class.getName());

    ClientPropertyRepository propertyRepo;
    ClientPropertyMapper     propertyMapper;

    @Autowired
    public ClientPropertyService(ClientPropertyRepository propertyRepo, ClientPropertyMapper propertyMapper)
    {
        this.propertyRepo = propertyRepo;
        this.propertyMapper = propertyMapper;
    }

    /**
     * Löscht alle in der Liste enthaltenen Mandanteneigenschaften.
     * 
     * @param detachedClientProperties
     *            Liste von zu löschenden Mandanteneigenschaften
     */
    public void deleteClientProperties(List<ClientPropertyDto> detachedClientProperties)
    {
        try
        {
            for (ClientPropertyDto clientProperty : detachedClientProperties)
            {
                ClientProperty persistentClientProperty = propertyRepo.findOne(clientProperty.getId());

                if (persistentClientProperty != null)
                {
                    propertyRepo.delete(persistentClientProperty);
                }
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }
    }

    /**
     * @param clientId
     *            Mandantenid
     * @return Alle Mandanteneigenschaften, welche zum Mandanten gehören
     */
    public List<ClientPropertyDto> getClientPropertiesForClientId(long clientId)
    {
        final List<ClientProperty> attachedClientProperties = new ArrayList<ClientProperty>();

        try
        {
            propertyRepo.findAllForClient(clientId).forEach(e -> attachedClientProperties.add(e));
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        AutoPopulatingList<ClientPropertyDto> result = new AutoPopulatingList<ClientPropertyDto>(
                ClientPropertyDto.class);

        for (ClientProperty attachedClientProperty : attachedClientProperties)
        {
            result.add(propertyMapper.mapToDto(attachedClientProperty));
        }

        return result;
    }

    /**
     * Speichert eine Liste von Mandanteneigenschaften.
     * 
     * @param detachedClientProperties
     *            Liste von MandantenMandanteneigenschaften
     */
    public void saveClientProperties(List<ClientPropertyDto> detachedClientProperties)
    {
        try
        {
            detachedClientProperties.forEach(e -> saveClientProperty(e));
        }
        catch (final Exception e)
        {
            logger.error(e);
        }
    }

    /**
     * Speichert eine Mandanteneigenschaft.
     * 
     * @param detachedClientProperty
     *            zu aktualisierende Mandanteneigenschaft
     * @return Aktualisierte Mandanteneigenschaft
     */
    public ClientPropertyDto saveClientProperty(ClientPropertyDto detachedClientProperty)
    {
        try
        {

            ClientProperty draftClientProperty = propertyMapper.mapToEntity(detachedClientProperty);
            ClientProperty persistedClientProperty = propertyRepo.save(draftClientProperty);

            if (persistedClientProperty != null)
            {
                return propertyMapper.mapToDto(persistedClientProperty);
            }
            else
            {
                return null;
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }
}
