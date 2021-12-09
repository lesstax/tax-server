package com.lesstax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lesstax.model.ClientEntity;
import com.lesstax.repositories.ClientCriteriaRepository;
import com.lesstax.repositories.ClientRepository;
import com.lesstax.request.response.model.ClientPage;
import com.lesstax.request.response.model.ClientResponse;
import com.lesstax.request.response.model.ClientSearchCriteria;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientCriteriaRepository clientCriteriaRepository;

	public ClientResponse getClients(ClientPage clientPage, ClientSearchCriteria clientSearchCriteria) {
		return new ClientResponse(clientCriteriaRepository.findAllWithFilters(clientPage, clientSearchCriteria), null,
				HttpStatus.OK);
	}

	public ClientEntity addClient(ClientEntity client) {
		return clientRepository.save(client);
	}
}