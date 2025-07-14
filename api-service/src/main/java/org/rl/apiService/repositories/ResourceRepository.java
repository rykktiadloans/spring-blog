package org.rl.apiService.repositories;

import org.rl.apiService.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository for fetching resource entities from the database
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    /**
     * Return a resource by its name
     * @param name Name of the resource
     * @return Specific resource
     */
    public Optional<Resource> findByName(String name);
}
