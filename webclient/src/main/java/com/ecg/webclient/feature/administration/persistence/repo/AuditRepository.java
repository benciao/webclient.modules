package com.ecg.webclient.feature.administration.persistence.repo;


import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.Audit;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Audit-Objekten.
 * 
 * @author arndtmar
 */
public interface AuditRepository extends CrudRepository<Audit, Long>
{
	@Query("select COUNT(a) from Audit a where a.user.id = :userId and a.authenticationOk = :authenticationOk")
    public Integer countLoginAttemptsForUser(@Param("userId") Long userId, @Param("authenticationOk") boolean authenticationOk);
	
	@Query("select a.occurance from Audit a where a.user.id = :userId and a.authenticationOk = :authenticationOk")
	public Iterable<Date> getLoginAttemptsTimeForUser(@Param("userId") Long userId, @Param("authenticationOk") boolean authenticationOk);
}
