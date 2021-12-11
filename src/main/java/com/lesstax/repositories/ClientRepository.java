package com.lesstax.repositories;

import com.lesstax.entity.ClientEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ClientRepository extends PagingAndSortingRepository<ClientEntity, Long> {

    Optional<ClientEntity> findById(Long id);

    Optional<ClientEntity> findByEmail(String email);
}
