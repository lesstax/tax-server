package com.lesstax.businessDelegate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientRepository;
import com.lesstax.request.model.ClientPaginationResponse;
import com.lesstax.service.SendEmailService;

@Service
public class ClientBusinessDelegate implements ClientBusinessRepository {

	private static final Logger logger = LogManager.getLogger(ClientBusinessDelegate.class);

	private static MessageDigest md;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SendEmailService sendEmail;

	private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	@Override
	public ClientModel saveClient(ClientModel clientModel) {

		logger.info("Inside saveClient() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		if (clientExitsOrNot(client.getEmail())) {
			logger.info("Exiting from saveClient() of Client BusinessDelegate");
			return null;
		} else {

			client.setCreatedBy(clientModel.getFirstName());
			client.setPassword(cryptWithMD5(client.getPassword()));
			client = clientRepository.save(client);
			sendEmail.sendOTPOnMail(client.getEmail(), client.getFirstName());
		}
		logger.info("Exiting from saveClient() of Client BusinessDelegate");
		return mapper.clientToClientModel(client);
	}

	@Override
	public ClientModel updateClient(ClientModel clientModel) {

		logger.info("Inside updateClient() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from updateClient() of Client BusinessDelegate");
		return mapper.clientToClientModel(client);
	}

	@Override
	public ClientModel emailVerified(ClientModel clientModel) {

		logger.info("Inside emailVerified() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		client.setIsEmailVerified(true);
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from emailVerified() of Client BusinessDelegate");
		return mapper.clientToClientModel(client);
	}

	@Override
	public List<ClientModel> getAllClient() {
		logger.info("Inside getAllClient() of Client BusinessDelegate");
		logger.info("Exiting from getAllClient() of Client BusinessDelegate");
		return mapper.clientToClientModelList((List<ClientEntity>) clientRepository.findAll());
	}

	@Override
	public ClientModel findClient(Long id) throws Exception {

		logger.info("Inside findClient() of Client BusinessDelegate");
		ClientEntity client = clientRepository.findById(id).orElseThrow(() -> new Exception("Client not available"));
		ClientModel clientModel = mapper.clientToClientModel(client);
		logger.info("Exiting from findClient() of Client BusinessDelegate");
		return clientModel;
	}

	@Override
	public ClientModel clientLogin(String email, String password) {

		logger.info("Inside clientLogin() of Client BusinessDelegate");
		ClientEntity client = clientRepository.findByEmail(email);
		password = cryptWithMD5(password);
		if (client != null && client.getPassword() != null && client.getPassword().equals(password)) {
			ClientModel clientModel = mapper.clientToClientModel(client);
			logger.info("Exiting from clientLogin() of Client BusinessDelegate");
			return clientModel;
		}
		logger.info("Exiting from clientLogin() of Client BusinessDelegate");
		return null;
	}

	@Override
	public ClientPaginationResponse getAllClients(Integer pageNo, Integer pageSize) {

		logger.info("Inside getAllClients() of Client BusinessDelegate");
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<ClientEntity> pagedResult = clientRepository.findAll(paging);

		if (pagedResult.hasContent()) {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			ClientPaginationResponse paginationResponse = new ClientPaginationResponse(pagedResult.getContent(),
					pagedResult.getTotalPages(), pagedResult.getNumber(), pagedResult.getSize());
			return paginationResponse;
		} else {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			return new ClientPaginationResponse();
		}
	}

	public ClientModel findClientByEmail(String clientEmailId) {
		logger.info("Inside findClientByEmail() of Client BusinessDelegate");
		logger.info("Exiting from findClientByEmail() of Client BusinessDelegate");
		ClientModel clientModel = mapper.clientToClientModel(clientRepository.findByEmail(clientEmailId));
		return clientModel;

	}

	public Boolean clientExitsOrNot(String emailId) {

		logger.info("Inside clientExitsOrNot() of Client BusinessDelegate");
		Optional<ClientEntity> client = Optional.ofNullable(clientRepository.findByEmail(emailId));
		if (client.isPresent()) {
			logger.info("Exiting from clientExitsOrNot() of Client BusinessDeleg");
			return true;
		}
		logger.info("Exiting from clientExitsOrNot() of Client BusinessDeleg");
		return false;
	}

	public static String cryptWithMD5(String pass) {
		logger.info("Inside cryptWithMD5() of Client BusinessDelegate");
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			logger.info("Exiting from cryptWithMD5() of Client BusinessDeleg");
		}
		return null;

	}

	@Override
	public Boolean removeClient(Long id) {
		logger.info("Inside removeClient() of Client BusinessDelegate");
		clientRepository.deleteById(id);	
		logger.info("Exiting from removeClient() of Client BusinessDeleg");
		return true;
	}

}
