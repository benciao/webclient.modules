package com.ecg.webclient.feature.administration.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.persistence.mapper.EnvironmentMapper;
import com.ecg.webclient.feature.administration.persistence.modell.Environment;
import com.ecg.webclient.feature.administration.persistence.repo.EnvironmentRepository;
import com.ecg.webclient.feature.administration.viewmodell.EnvironmentDto;

/**
 * Service zum Bearbeiten von Umgebungen und deren Eigenschaften.
 * 
 * @author arndtmar
 */
@Component
public class EnvironmentService
{
    static final Logger   logger = LogManager.getLogger(EnvironmentService.class.getName());

    EnvironmentRepository environmentRepo;
    EnvironmentMapper     environmentMapper;

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepo, EnvironmentMapper environmentMapper)
    {
        this.environmentRepo = environmentRepo;
        this.environmentMapper = environmentMapper;
    }

    /**
     * @return Die aktuelle Systemumgebung
     */
    public EnvironmentDto getEnvironment()
    {
        try
        {
            Iterable<Environment> environment = environmentRepo.findAll();
            return (environment != null && environment.iterator().hasNext()) ? environmentMapper
                    .mapToDto(environment.iterator().next()) : null;
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    /**
     * Speichert eine Systemumgebung
     * 
     * @param detachedEnvironment
     *            zu aktualisierende Umgebung
     * @return Aktualisierte Umgebung
     */
    public EnvironmentDto saveEnvironment(EnvironmentDto detachedEnvironment)
    {
        try
        {
            Environment draftEnvironment = environmentMapper.mapToEntity(detachedEnvironment);
            Environment persistedEnvironment = environmentRepo.save(draftEnvironment);

            if (persistedEnvironment != null)
            {
                return environmentMapper.mapToDto(persistedEnvironment);
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
