package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.ClientProperty;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Property-Objekten.
 * 
 * @author arndtmar
 */
public interface ClientPropertyRepository extends CrudRepository<ClientProperty, Long>
{
    @Modifying
    @Query("delete from ClientProperty cp where cp.client.id = :clientId")
    public void deleteForClient(@Param("clientId") Long clientId);

    @Query("select cp from ClientProperty cp where cp.client.id = :clientId")
    public Iterable<ClientProperty> findAllForClient(@Param("clientId") Long clientId);
}
