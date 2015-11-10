package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.ClientProperty;
import com.ecg.webclient.feature.administration.persistence.repo.ClientPropertyRepository;
import com.ecg.webclient.feature.administration.viewmodell.ClientPropertyDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf eine detachted Eigenschaft oder
 * umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class ClientPropertyMapper
{
    @Autowired
    ClientPropertyRepository propertyRepo;
    @Autowired
    ClientMapper             clientMapper;

    /**
     * Wandelt eine attachte Eigenschaft in eine detachte um.
     * 
     * @param property
     *            attachte Eigenschaft
     * @return Detachete Eigenschaft
     */
    public ClientPropertyDto mapToDto(ClientProperty property)
    {
        ClientPropertyDto dto = new ClientPropertyDto();
        dto.setKey(property.getKey());
        dto.setValue(property.getValue());
        dto.setId(property.getId());
        dto.setDelete(false);
        dto.setClient(clientMapper.mapToDto(property.getClient()));

        return dto;
    }

    /**
     * Wandelt eine Liste von attachten Eigenschaften in eine Liste von detachten Eigenschaften um.
     * 
     * @param properties
     *            Liste von attachten Eigenschaften
     * @return Liste von detachten Eigenschaften
     */
    public List<ClientPropertyDto> mapToDtos(List<ClientProperty> properties)
    {
        List<ClientPropertyDto> result = new AutoPopulatingList<ClientPropertyDto>(ClientPropertyDto.class);

        properties.forEach(e -> result.add(mapToDto(e)));

        return result;
    }

    /**
     * Wandelt eine Liste von detachten Eigenschaften in eine Liste von attachten Eigenschaften um.
     * 
     * @param dtos
     *            Liste von detachten Eigenschaften
     * @return Liste von attachten Eigenschaften
     */
    public List<ClientProperty> mapToEntities(List<ClientPropertyDto> dtos)
    {
        List<ClientProperty> result = new ArrayList<ClientProperty>();

        dtos.forEach(e -> result.add(mapToEntity(e)));

        return result;
    }

    /**
     * Wandelt eine detachte Eigenschaft in eine attachte um.
     * 
     * @param dto
     *            Detachte Eigenschaft
     * @return attachte Eigenschaft
     */
    public ClientProperty mapToEntity(ClientPropertyDto dto)
    {
        ClientProperty property = new ClientProperty();
        property.setId(dto.getId());
        property.setKey(dto.getKey());
        property.setValue(dto.getValue());
        property.setClient(clientMapper.mapToEntity(dto.getClient()));

        ClientProperty persistentProperty = propertyRepo.findOne(property.getId());
        if (persistentProperty != null)
        {
            return persistentProperty.bind(property);
        }

        return property;
    }
}
