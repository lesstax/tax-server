package com.lesstax.businessDelegate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lesstax.businessRepositories.ClientBusinessRepository;
import com.lesstax.email.SendEmail;
import com.lesstax.mapper.ClientMapper;
import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientRepository;

@Service
public class ClientBusinessDelegate implements ClientBusinessRepository {

	private static final Logger logger = LogManager.getLogger(ClientBusinessDelegate.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SendEmail sendEmail;

	private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	@Override
	public Client saveClient(Client client) {

		logger.info("Inside saveClient() of Client BusinessDelegate");

		Client clientEmailCheck = clientRepository.findByEmail(client.getEmail());
		if (clientEmailCheck != null) {
			logger.info("Exiting from saveClient() of Client BusinessDelegate");
			return null;
		}

		client = clientRepository.save(client);
		if (client != null) {
			sendEmail.sendOTPOnMail(client.getEmail(), client.getFirstName());
		}
		logger.info("Exiting from saveClient() of Client BusinessDelegate");
		return client;
	}

	@Override
	public List<Client> getAllClient() {
		logger.info("Inside getAllClient() of Client BusinessDelegate");
		logger.info("Exiting from getAllClient() of Client BusinessDelegate");
		return (List<Client>) clientRepository.findAll();
	}

	@Override
	public ClientModel findClient(Long id) throws Exception {

		logger.info("Inside findClient() of Client BusinessDelegate");
		Client client = clientRepository.findById(id).orElseThrow(() -> new Exception("Client not available"));
		ClientModel clientMapper = mapper.clientToClientModel(client);
		logger.info("Exiting from findClient() of Client BusinessDelegate");
		return clientMapper;
	}

	@Override
	public ClientModel clientLogin(String email, String password) {

		logger.info("Inside clientLogin() of Client BusinessDelegate");
		Client client = clientRepository.findByEmail(email);
		if (client != null && client.getPassword().equals(password)) {
			ClientModel clientMapper = mapper.clientToClientModel(client);
			logger.info("Exiting from clientLogin() of Client BusinessDelegate");
			return clientMapper;
		}
		logger.info("Exiting from clientLogin() of Client BusinessDelegate");
		return null;
	}

	@Override
	public List<Client> getAllClients(Integer pageNo, Integer pageSize) {

		Pageable paging = PageRequest.of(pageNo, pageSize);

		Page<Client> pagedResult = clientRepository.findAll(paging);

		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Client>();
		}
	}

}
