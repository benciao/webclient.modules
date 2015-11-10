package com.ecg.webclient.feature.administration.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.Role;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Rollen-Objekten.
 * 
 * @author arndtmar
 */
public interface RoleRepository extends CrudRepository<Role, Long>
{
	@Query("select r from Role r where r.enabled = true and r.feature.enabled = true")
	public Iterable<Role> findAllEnabledRoles();

	@Query("select r from Role r where r.feature.enabled = true")
	public Iterable<Role> findAllRoles();

	@Query("select r from Role r where r.name = :name and r.feature.name = :feature_name")
	public Role findRoleByNameAndFeature(@Param("name") String name, @Param("feature_name") String featureName);

	@Query("select r from Role r where r.id in :ids and r.feature.enabled = true")
	public Iterable<Role> findEnabledRolesWithEnabledFeatureForIds(@Param("ids") List<Long> roleIds);
}
