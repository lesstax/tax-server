package com.lesstax.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lesstax.businessDelegate.ClientBusinessDelegate;
import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientBusinessDelegate clientBusinessDelegate;

	@PostMapping("/save")
	public Client saveClient(@RequestBody Client client) {
		logger.info("Inside saveClient() of client controller");
		logger.info("Exiting from saveClient() of client controller");
		return clientBusinessDelegate.saveClient(client);
	}

	@GetMapping
	public List<Client> getAllClients() {
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

	@GetMapping("/clientLogin")
	public ClientModel clientLogin(@RequestParam String email, @RequestParam String password) throws Exception {
		logger.info("Inside clientLogin() of client controller");
		ClientModel clientMapper = clientBusinessDelegate.clientLogin(email, password);
		logger.info("Exiting from clientLogin() of client controller");
		return clientMapper;
	}

	@GetMapping("/getClientsWithPagination")
	public List<Client> getAllEmployees(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		List<Client> clients = clientBusinessDelegate.getAllClients(pageNo, pageSize);
		return clients;
	}

}
