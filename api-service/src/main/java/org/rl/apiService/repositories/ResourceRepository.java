package org.rl.apiService.repositories;

import org.rl.apiService.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    public Optional<Resource> findByName(String name);
}
