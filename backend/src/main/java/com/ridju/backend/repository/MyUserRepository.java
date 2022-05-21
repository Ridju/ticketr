package com.ridju.backend.repository;

import com.ridju.backend.domain.model.MyUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends PagingAndSortingRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
    Boolean existsByUsername(String username);
}