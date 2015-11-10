package com.ecg.webclient.feature.administration.persistence.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.RemoteLogin;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf
 * FremdsystemLogin-Objekten.
 * 
 * @author arndtmar
 */
public interface RemoteLoginRepository extends CrudRepository<RemoteLogin, Long>
{
	@Modifying
	@Query("delete from RemoteLogin r where r.remoteSystem.id = :rmid")
	public void deleteAllForRemoteSystemId(@Param("rmid") Long remoteSystemId);

	@Query("select r from RemoteLogin r where r.user.id = :userId")
	public Iterable<RemoteLogin> findAllForForUserId(@Param("userId") Long userId);

	@Query("select r from RemoteLogin r where r.remoteSystem.id = :rmid")
	public Iterable<RemoteLogin> findAllForRemoteSystemId(@Param("rmid") Long remoteSystemId);

	@Query("select r from RemoteLogin r where r.user.id = :userId and r.enabled = true")
	public Iterable<RemoteLogin> findEnabledForForUserId(@Param("userId") Long userId);

    @Modifying
	@Query("update RemoteLogin r set r.remoteSystemLoginName = :login, r.remoteSystemPassword = :pw, r.enabled = :enabled where r.id = :id")
	public void updateForId(@Param("id") Long id, @Param("login") String login, @Param("pw") String pw,
			@Param("enabled") boolean enabled);
}
