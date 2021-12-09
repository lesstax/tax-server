package com.lesstax.controller;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lesstax.businessDelegate.ClientBusinessDelegate;
import com.lesstax.exception.BusinessException;
import com.lesstax.exception.ErrorVO;
import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientCriteriaRepository;
import com.lesstax.request.response.model.ClientPage;
import com.lesstax.request.response.model.ClientPaginationRequest;
import com.lesstax.request.response.model.ClientPaginationWithFIlter;
import com.lesstax.request.response.model.ClientResponse;
import com.lesstax.request.response.model.ClientSignIn;
import com.lesstax.request.response.model.OTPModel;
import com.lesstax.request.response.model.ResetPasswordRequest;
import com.lesstax.service.ClientService;
import com.lesstax.service.SendOTPService;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private static Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientBusinessDelegate clientBusinessDelegate;

	@Autowired
	private SendOTPService sendOTPService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientCriteriaRepository clientCriteriaRepository;

	@PostMapping("/signup")
	public ClientResponse signUp(@RequestBody ClientModel clientModel) {
		
		logger.info("Inside signUp() of client controller");
		logger.info("Exiting from signUp() of client controller");
		return clientBusinessDelegate.saveClient(clientModel);
	}

	@GetMapping
	public ClientResponse getAllClients() {
		
		logger.info("Inside getAllClients() of client controller");
		logger.info("Exiting from getAllClients() of client controller");
		return clientBusinessDelegate.getAllClient();
	}

	@GetMapping("/{id}")
	public ClientResponse findClient(@PathVariable Long id) throws Exception {

		logger.info("Inside findClient() of client controller");
		logger.info("Exiting from findClient() of client controller");
		return clientBusinessDelegate.findClientById(id);
	}

	@PostMapping("/signin")
	public ClientResponse signIn(@RequestBody ClientSignIn clientSignIn) throws BusinessException {

		logger.info("Inside signIn() of client controller");
		logger.info("Exiting from signIn() of client controller");
		return clientBusinessDelegate.clientLogin(clientSignIn.getEmailId(), clientSignIn.getPassword());
	}

	@PostMapping("/getClientsWithPagination")
	public ClientResponse getClientsWithPagination(@RequestBody ClientPaginationRequest paginationRequest) {
		
		logger.info("Inside getClientsWithPagination() of client controller");
		logger.info("Exiting from getClientsWithPagination() of client controller");
		return clientBusinessDelegate.getAllClients(paginationRequest.getPageNo(), paginationRequest.getPageSize());
	}

	@PostMapping("/getClientsWithPaginationAndFilter")
	public ClientResponse getClientsWithPaginationAndFilter(
			@RequestBody ClientPaginationWithFIlter clientPaginationWithFIlter) {
		
		logger.info("Inside getClientsWithPaginationAndFilter() of client controller");
		logger.info("Exiting from getClientsWithPaginationAndFilter() of client controller");
		return clientService.getClients(clientPaginationWithFIlter.getClientPage(),
				clientPaginationWithFIlter.getClientSearchCriteria());
	}

	@PostMapping("/verifyOtp")
	public ClientResponse verifyOtp(@RequestBody OTPModel otpModel) {
		
		logger.info("Inside verifyOtp() of client controller");
		Integer storedOtp = sendOTPService.getOtp(otpModel.getClientEmail());
		if (otpModel.getOtp().intValue() == storedOtp.intValue()) {
			return clientBusinessDelegate
					.emailVerified(clientBusinessDelegate.findClientByEmail(otpModel.getClientEmail()));
		} else {
			return new ClientResponse(null,
					new BusinessException(Arrays.asList(new ErrorVO("04", "Your OTP is incorrect"))), HttpStatus.OK);
		}
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

	@PostMapping("/updateClient")
	public ClientResponse updateClient(@RequestBody ClientModel clientModel) {
		
		logger.info("Inside updateClient() of client controller");
		logger.info("Exiting from updateClient() of client controller");
		return clientBusinessDelegate.updateClient(clientModel);
	}
	
	@PostMapping("/resetPassword")
	public ClientResponse resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		
		logger.info("Inside resetPassword() of client controller");
		logger.info("Exiting from resetPassword() of client controller");
		return clientBusinessDelegate.updatePassword(resetPasswordRequest);
	}
	
	@GetMapping("/forgotPassword")
	public ClientResponse forgotPassword(@RequestParam String emailId) {
		
		logger.info("Inside updateClient() of client controller");
		logger.info("Exiting from updateClient() of client controller");
		return clientBusinessDelegate.sendTemporaryPassword(emailId);
	}
}
