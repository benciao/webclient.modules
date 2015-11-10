package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Environment;
import com.ecg.webclient.feature.administration.persistence.repo.EnvironmentRepository;
import com.ecg.webclient.feature.administration.viewmodell.EnvironmentDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf eine detachte Umgebung oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class EnvironmentMapper
{
    @Autowired
    EnvironmentRepository environmentRepo;

    /**
     * Wandelt eine attachte Umgebung in eine detachte um.
     * 
     * @param environment
     *            attachte Umgebung
     * @return Detachete Umgebung
     */
    public EnvironmentDto mapToDto(Environment environment)
    {
        EnvironmentDto dto = new EnvironmentDto();
        dto.setPasswordChangeInterval(environment.getPasswordChangeInterval());
        dto.setId(environment.getId());
        dto.setDelete(false);
        dto.setAllowedLoginAttempts(environment.getAllowedLoginAttempts());
        dto.setSystemIdentifier(environment.getSystemIdentifier());

        return dto;
    }

    /**
     * Wandelt eine Liste von attachten Umbegungen in eine Liste von detachten Umbegungen um.
     * 
     * @param environments
     *            Liste von attachten Umbegungen
     * @return Liste von detachten Umbegungen
     */
    public List<EnvironmentDto> mapToDtos(List<Environment> environments)
    {
        List<EnvironmentDto> result = new AutoPopulatingList<EnvironmentDto>(EnvironmentDto.class);

        environments.forEach(e -> result.add(mapToDto(e)));

        return result;
    }

    /**
     * Wandelt eine Liste von detachten Umgebungen in eine Liste von attachten Umgebungen um.
     * 
     * @param dtos
     *            Liste von detachten Umgebungen
     * @return Liste von attachten Umgebungen
     */
    public List<Environment> mapToEntities(List<EnvironmentDto> dtos)
    {
        List<Environment> result = new ArrayList<Environment>();

        dtos.forEach(e -> result.add(mapToEntity(e)));

        return result;
    }

    /**
     * Wandelt eine detachte Umgebung in eine attachte um.
     * 
     * @param dto
     *            Detachte Umgebung
     * @return attachte Umgebung
     */
    public Environment mapToEntity(EnvironmentDto dto)
    {
        Environment environment = new Environment();
        environment.setId(dto.getId());
        environment.setPasswordChangeInterval(dto.getPasswordChangeInterval());
        environment.setAllowedLoginAttempts(dto.getAllowedLoginAttempts());
        environment.setSystemIdentifier(dto.getSystemIdentifier());

        Environment persistentEnvironment = environmentRepo.findOne(environment.getId());
        if (persistentEnvironment != null)
        {
            return persistentEnvironment.bind(environment);
        }

        return environment;
    }
}
