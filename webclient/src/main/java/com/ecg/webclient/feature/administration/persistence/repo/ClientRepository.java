package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.Client;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Mandanten-Objekten.
 * 
 * @author arndtmar
 */
public interface ClientRepository extends CrudRepository<Client, Long>
{
	@Query("select c from Client c where c.enabled = :enabled")
    public Iterable<Client> findAllEnabledClients(@Param("enabled") boolean isEnabled);
	
	public Client findClientByName(String name);
}
