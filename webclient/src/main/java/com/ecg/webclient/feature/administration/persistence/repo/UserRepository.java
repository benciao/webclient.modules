package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.User;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Benutzer-Objekten.
 * 
 * @author arndtmar
 */
public interface UserRepository extends CrudRepository<User, Long>
{
	@Query("select u from User u where u.enabled = :enabled")
	public Iterable<User> findAllEnabledUsers(@Param("enabled") boolean isEnabled);

	public User findUserByLogin(String login);

    @Query("select u.password from User u where u.id = :id")
    public String getPassword(@Param("id") Long id);
}
