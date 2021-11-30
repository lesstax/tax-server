package com.lesstax.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lesstax.model.Client;

public interface ClientRepository  extends PagingAndSortingRepository<Client, Long> {

	Optional<Client> findById(Long id);

	 Client findByEmail(String email);
}
