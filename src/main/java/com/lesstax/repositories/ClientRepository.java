package com.lesstax.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lesstax.model.ClientEntity;

public interface ClientRepository  extends PagingAndSortingRepository<ClientEntity, Long> {

	Optional<ClientEntity> findById(Long id);

	 ClientEntity findByEmail(String email);
}
