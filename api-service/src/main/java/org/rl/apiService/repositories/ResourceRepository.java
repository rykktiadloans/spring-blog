package org.rl.apiService.repositories;

import org.rl.apiService.model.Post;
import org.rl.apiService.model.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * Find a list of resource whose names match the ones supplied
     * @param names Names to check
     * @return List of matching resources.
     */
    public List<Resource> findByNameIn(List<String> names);

    /**
     * Get a list of resources using a page, sorted alphabetically
     * @param pageable Pageable object
     * @return List of resources
     */
    List<Resource> findAllByOrderByNameDesc(Pageable pageable);

}
