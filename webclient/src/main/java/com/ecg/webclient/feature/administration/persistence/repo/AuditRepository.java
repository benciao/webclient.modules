package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ecg.webclient.feature.administration.persistence.modell.Audit;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Audit-Objekten.
 * 
 * @author arndtmar
 */
public interface AuditRepository extends CrudRepository<Audit, Long>
{

}
