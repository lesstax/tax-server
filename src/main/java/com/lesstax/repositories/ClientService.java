package com.lesstax.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lesstax.model.ClientEntity;
import com.lesstax.request.model.ClientPage;
import com.lesstax.request.model.ClientSearchCriteria;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientCriteriaRepository clientCriteriaRepository;

	public Page<ClientEntity> getClients(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
		return clientCriteriaRepository.findAllWithFilters(clientPage, clientSearchCriteria);
	}

	public ClientEntity addClient(ClientEntity client) {
		return clientRepository.save(client);
	}
}