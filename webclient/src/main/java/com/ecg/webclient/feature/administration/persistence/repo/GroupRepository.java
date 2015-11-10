package com.ecg.webclient.feature.administration.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecg.webclient.feature.administration.persistence.modell.Group;

/**
 * Repository zum Bereitstellen von CRUD-Operationen auf Gruppen-Objekten.
 * 
 * @author arndtmar
 */
public interface GroupRepository extends CrudRepository<Group, Long>
{
    @Query("select g from Group g where g.enabled = :enabled")
    public Iterable<Group> findAllEnabledGroups(@Param("enabled") boolean isEnabled);

    @Query("select g from Group g where g.client.id = :id")
    public Iterable<Group> findAllGroupsAssignedToClientId(@Param("id") long clientId);

    @Query("select g from Group g where g.enabled = true and g.id in :ids")
    public Iterable<Group> findEnabledGroupsForId(@Param("ids") List<Long> groupIds);

    @Query("select g from Group g where g.name = :name")
    public Iterable<Group> findGroupByName(@Param("name") String groupName);

    @Query("select g from Group g where g.name = :name and g.client.id = :clientId")
    public Group findGroupByNameForClient(@Param("name") String groupName, @Param("clientId") long clientId);
}
