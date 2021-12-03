package com.lesstax.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lesstax.model.Client;
import com.lesstax.request.model.ClientPage;
import com.lesstax.request.model.ClientSearchCriteria;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientCriteriaRepository clientCriteriaRepository;

	public Page<Client> getClients(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
		return clientCriteriaRepository.findAllWithFilters(clientPage, clientSearchCriteria);
	}

	public Client addClient(Client client) {
		return clientRepository.save(client);
	}
}