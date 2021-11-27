package com.lesstax.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import com.lesstax.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

	Optional<Client> findById(Long id);

	 Client findByEmail(String email);
}
