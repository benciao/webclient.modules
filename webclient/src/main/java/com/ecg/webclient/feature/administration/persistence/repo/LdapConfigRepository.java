package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ecg.webclient.feature.administration.persistence.modell.LdapConfig;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf LdapConfig-Objekten.
 * 
 * @author arndtmar
 */
public interface LdapConfigRepository extends CrudRepository<LdapConfig, Long>
{

}
