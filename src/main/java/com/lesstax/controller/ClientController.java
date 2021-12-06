package com.lesstax.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lesstax.businessDelegate.ClientBusinessDelegate;
import com.lesstax.exception.LessTaxException;
import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientCriteriaRepository;
import com.lesstax.repositories.ClientService;
import com.lesstax.request.model.ClientPage;
import com.lesstax.request.model.ClientPaginationRequest;
import com.lesstax.request.model.ClientPaginationResponse;
import com.lesstax.request.model.ClientPaginationWithFIlter;
import com.lesstax.request.model.ClientSignIn;
import com.lesstax.request.model.OTPModel;
import com.lesstax.service.SendOTPService;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientBusinessDelegate clientBusinessDelegate;

	@Autowired
	private SendOTPService sendOTPService;

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ClientCriteriaRepository clientCriteriaRepository;

	@PostMapping("/signup")
	public ClientModel signUp(@RequestBody ClientModel clientModel) throws LessTaxException {
		logger.info("Inside signUp() of client controller");
		logger.info("Exiting from signUp() of client controller");
		return clientBusinessDelegate.saveClient(clientModel);
	}

	@GetMapping
	public List<ClientModel> getAllClients() {
		logger.info("Inside getAllClients() of client controller");
		logger.info("Exiting from getAllClients() of client controller");
		return clientBusinessDelegate.getAllClient();
	}

	@GetMapping("/{id}")
	public ClientModel findClient(@PathVariable Long id) throws Exception {
		logger.info("Inside findClient() of client controller");
		ClientModel clientMapper = clientBusinessDelegate.findClient(id);
		logger.info("Exiting from findClient() of client controller");
		return clientMapper;
	}

	@PostMapping("/signin")
	public ClientModel signIn(@RequestBody ClientSignIn clientSignIn) throws Exception {
		logger.info("Inside signIn() of client controller");
		ClientModel clientMapper = clientBusinessDelegate.clientLogin(clientSignIn.getEmailId(),
				clientSignIn.getPassword());
		logger.info("Exiting from signIn() of client controller");
		return clientMapper;
	}

	@PostMapping("/getClientsWithPagination")
	public ClientPaginationResponse getClientsWithPagination(@RequestBody ClientPaginationRequest paginationRequest) {
		logger.info("Inside getClientsWithPagination() of client controller");
		ClientPaginationResponse clients = clientBusinessDelegate.getAllClients(paginationRequest.getPageNo(),
				paginationRequest.getPageSize());
		logger.info("Exiting from getClientsWithPagination() of client controller");
		return clients;
	}

	@PostMapping("/getClientsWithPaginationAndFilter")
	public Page<ClientEntity> getClientsWithPaginationAndFilter(
			@RequestBody ClientPaginationWithFIlter clientPaginationWithFIlter) {
		logger.info("Inside getClientsWithPaginationAndFilter() of client controller");
		logger.info("Exiting from getClientsWithPaginationAndFilter() of client controller");
		return clientService.getClients(clientPaginationWithFIlter.getClientPage(),
				clientPaginationWithFIlter.getClientSearchCriteria());
	}

	@PostMapping("/verifyOtp")
	public ClientModel verifyOtp(@RequestBody OTPModel otpModel) throws Exception {
		logger.info("Inside verifyOtp() of client controller");

		Integer storedOtp = sendOTPService.getOtp(otpModel.getClientEmail());
		if (otpModel.getOtp().intValue() == storedOtp.intValue()) {
			ClientModel clientModel = clientBusinessDelegate.findClientByEmail(otpModel.getClientEmail());
			if (clientModel != null) {
				return clientBusinessDelegate.emailVerified(clientModel);
			}
		} else {
			logger.info("Exiting from verifyOtp() of client controller");
			return null;
		}
		logger.info("Exiting from verifyOtp() of client controller");
		return null;
	}
	
	@PostMapping("/getAll")
	public Page<ClientEntity> getAll(@RequestBody ClientPage clientPage) {
		logger.info("Inside getClientsWithPagination() of client controller");
		Page<ClientEntity> clients = clientCriteriaRepository.getAllClients(clientPage);
		logger.info("Exiting from getClientsWithPagination() of client controller");
		return clients;
	}
	
	@GetMapping("/removeClient/{id}")
	public Boolean removeClient(@PathVariable Long id) {
		logger.info("Inside removeClient() of client controller");
		logger.info("Exiting from removeClient() of client controller");
		return clientBusinessDelegate.removeClient(id);
	}

}
