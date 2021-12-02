package com.lesstax.businessDelegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lesstax.businessRepositories.ClientBusinessRepository;
import com.lesstax.mapper.ClientMapper;
import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientRepository;
import com.lesstax.request.model.ClientPaginationRequest;
import com.lesstax.request.model.ClientPaginationResponse;
import com.lesstax.service.SendEmailService;

@Service
public class ClientBusinessDelegate implements ClientBusinessRepository {

	private static final Logger logger = LogManager.getLogger(ClientBusinessDelegate.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SendEmailService sendEmail;

	private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	@Override
	public Client saveClient(Client client) {

		logger.info("Inside saveClient() of Client BusinessDelegate");

		Optional<Client> clientEmailCheck = Optional.ofNullable(clientRepository.findByEmail(client.getEmail()));
		if (clientEmailCheck.isPresent()) {
			logger.info("Exiting from saveClient() of Client BusinessDelegate");
			return null;
		} else {

			client.setCreatedDate(getDateAndTime());
			client.setCreatedBy(client.getFirstName());
			client = clientRepository.save(client);
			sendEmail.sendOTPOnMail(client.getEmail(), client.getFirstName());
		}
		logger.info("Exiting from saveClient() of Client BusinessDelegate");
		return client;
	}

	@Override
	public Client updateClient(Client client) {

		logger.info("Inside updateClient() of Client BusinessDelegate");
		client.setUpdateDate(getDateAndTime());
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from updateClient() of Client BusinessDelegate");
		return client;
	}

	@Override
	public Client emailVerified(Client client) {

		logger.info("Inside emailVerified() of Client BusinessDelegate");
		client.setIsEmailVerified(true);
		client.setUpdateDate(getDateAndTime());
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from emailVerified() of Client BusinessDelegate");
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
	public ClientPaginationResponse getAllClients(Integer pageNo, Integer pageSize) {

		logger.info("Inside getAllClients() of Client BusinessDelegate");
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Client> pagedResult = clientRepository.findAll(paging);

		if (pagedResult.hasContent()) {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			ClientPaginationResponse paginationResponse = new ClientPaginationResponse(pagedResult.getContent(),
					pagedResult.getTotalPages(),pagedResult.getNumber(),pagedResult.getSize());
			return paginationResponse;
		} else {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			return new ClientPaginationResponse();
		}
	}

	public Client findClientByEmail(String clientEmailId) {
		logger.info("Inside findClientByEmail() of Client BusinessDelegate");
		logger.info("Exiting from findClientByEmail() of Client BusinessDelegate");
		return clientRepository.findByEmail(clientEmailId);

	}

	public Date getDateAndTime() {
		Date date = java.util.Calendar.getInstance().getTime();
		return date;
	}
}
