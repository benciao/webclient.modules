package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ecg.webclient.feature.administration.persistence.modell.Environment;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Environment-Objekten.
 * 
 * @author arndtmar
 */
public interface EnvironmentRepository extends CrudRepository<Environment, Long>
{

}
