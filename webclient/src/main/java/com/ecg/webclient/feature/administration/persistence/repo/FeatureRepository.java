package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.Feature;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Feature-Objekten.
 * 
 * @author arndtmar
 */
public interface FeatureRepository extends CrudRepository<Feature, Long>
{
	@Query("select f from Feature f where f.name = :name")
	public Feature findFeatureByName(@Param("name") String name);

	@Query("select f from Feature f where f.enabled = :enabled")
	public Iterable<Feature> findAllEnabled(@Param("enabled") boolean isEnabled);
}
