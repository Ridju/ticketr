package com.ridju.backend.repository;

import com.ridju.backend.domain.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Boolean existsByName(String name);
}
