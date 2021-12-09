package com.lesstax.businessDelegate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lesstax.businessRepositories.ClientBusinessRepository;
import com.lesstax.exception.BusinessException;
import com.lesstax.exception.ErrorVO;
import com.lesstax.mapper.ClientMapper;
import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.repositories.ClientRepository;
import com.lesstax.request.response.model.ClientPaginationResponse;
import com.lesstax.request.response.model.ClientResponse;
import com.lesstax.request.response.model.ResetPasswordRequest;
import com.lesstax.service.SendEmailService;
import com.lesstax.service.SendOTPService;
import com.lesstax.utility.BusinessUtility;

@Service
public class ClientBusinessDelegate implements ClientBusinessRepository {

	private static final Logger logger = LogManager.getLogger(ClientBusinessDelegate.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SendEmailService sendEmail;

	@Autowired
	private SendOTPService sendOTPService;
	
	private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	@Override
	public ClientResponse saveClient(ClientModel clientModel) throws BusinessException {
		logger.info("Inside saveClient() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
			throw new BusinessException(Arrays.asList(new ErrorVO("01", "Client Already found with this email id")));
		} else {
			client.setCreatedBy(clientModel.getFirstName());
			client.setPassword(BusinessUtility.cryptWithMD5(client.getPassword()));
			client = clientRepository.save(client);
			sendEmail.sendOTPOnMail(client.getEmail(), client.getFirstName(),"OTP");
			return new ClientResponse(mapper.clientToClientModel(client), null, HttpStatus.OK);
		}
	}

	@Override
	public ClientResponse updateClient(ClientModel clientModel) throws BusinessException {

		logger.info("Inside updateClient() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from updateClient() of Client BusinessDelegate");
		return new ClientResponse(mapper.clientToClientModel(client), null, HttpStatus.OK);
	}

	@Override
	public ClientResponse emailVerified(ClientModel clientModel) throws BusinessException {

		logger.info("Inside emailVerified() of Client BusinessDelegate");
		ClientEntity client = mapper.clientModelToClient(clientModel);
		client.setIsEmailVerified(true);
		client.setUpdatedBy(client.getFirstName());
		clientRepository.save(client);
		logger.info("Exiting from emailVerified() of Client BusinessDelegate");
		return new ClientResponse(mapper.clientToClientModel(client), null, HttpStatus.OK);
	}

	@Override
	public ClientResponse getAllClient() throws BusinessException {

		logger.info("Inside getAllClient() of Client BusinessDelegate");
		logger.info("Exiting from getAllClient() of Client BusinessDelegate");
		return new ClientResponse(mapper.clientToClientModelList((List<ClientEntity>) clientRepository.findAll()), null,
				HttpStatus.OK);
	}

	@Override
	public ClientResponse findClientById(Long id) throws BusinessException, Exception {

		logger.info("Inside findClient() of Client BusinessDelegate");
		ClientEntity client = clientRepository.findById(id).orElseThrow(() -> new Exception("Client not available"));
		logger.info("Exiting from findClient() of Client BusinessDelegate");
		return new ClientResponse(mapper.clientToClientModel(client), null, HttpStatus.OK);
	}

	@Override
	public ClientResponse clientLogin(String email, String password) throws BusinessException {

		logger.info("Inside clientLogin() of Client BusinessDelegate");
		Optional<ClientEntity> client = clientRepository.findByEmail(email);
		if(client.isPresent() && client.get().getIsForgotPasswordStatus() == true){
			logger.info("Exiting from clientLogin() of Client BusinessDelegate");
			throw new BusinessException(Arrays.asList(new ErrorVO("05", "Please reset your passowrd")));
		}else {
			password = BusinessUtility.cryptWithMD5(password);
			if (client.isPresent() && null != client.get().getPassword() && client.get().getPassword().equals(password)) {
				logger.info("Exiting from clientLogin() of Client BusinessDelegate");
				return new ClientResponse(mapper.clientToClientModel(client.get()), null, HttpStatus.OK);
			} else{
				throw  new BusinessException(Arrays.asList(new ErrorVO("02", "Your email id or password is incorreect")));
			}
		}
	}


	@Override
	public ClientResponse getAllClients(Integer pageNo, Integer pageSize) throws BusinessException {

		logger.info("Inside getAllClients() of Client BusinessDelegate");
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<ClientEntity> pagedResult = clientRepository.findAll(paging);

		if (pagedResult.hasContent()) {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			return new ClientResponse(new ClientPaginationResponse(pagedResult.getContent(),
					pagedResult.getTotalPages(), pagedResult.getNumber(), pagedResult.getSize()), null, HttpStatus.OK);
		} else {
			logger.info("Exiting from getAllClients() of Client BusinessDelegate");
			return new ClientResponse(null, new BusinessException(Arrays.asList(new ErrorVO("03", "No Record Found"))),
					HttpStatus.OK);
		}
	}

	@Override
	public ClientModel findClientByEmail(String clientEmailId) {

		logger.info("Inside findClientByEmail() of Client BusinessDelegate");
		logger.info("Exiting from findClientByEmail() of Client BusinessDelegate");
		return mapper.clientToClientModel(clientRepository.findByEmail(clientEmailId).get());

	}

	@Override
	public Boolean removeClient(Long id) {

		logger.info("Inside removeClient() of Client BusinessDelegate");
		clientRepository.deleteById(id);
		logger.info("Exiting from removeClient() of Client BusinessDeleg");
		return true;
	}

	public ClientResponse sendTemporaryPassword(String clientEmailId) throws BusinessException {
		logger.info("Inside sendTemporaryPassword() of Client BusinessDelegate");
		Optional<ClientEntity> client = clientRepository.findByEmail(clientEmailId);
		client.orElseThrow(()-> new BusinessException(Arrays.asList(new ErrorVO("04", "No Client found with this email id"))));
		sendEmail.sendOTPOnMail(clientEmailId, null,"password");
		Integer storedOtp = sendOTPService.getOtp(clientEmailId);
		String password = Integer.toString(storedOtp);
		client.get().setPassword(BusinessUtility.cryptWithMD5(password));
		client.get().setIsForgotPasswordStatus(true);
		client = Optional.of(clientRepository.save(client.get()));
		logger.info("Exiting from sendTemporaryPassword() of Client BusinessDeleg");
		return new ClientResponse(mapper.clientToClientModel(client.get()), null, HttpStatus.OK);
	}

	public ClientResponse updatePassword(ResetPasswordRequest resetPasswordRequest) {
		logger.info("Inside updatePassword() of Client BusinessDelegate");
		Optional<ClientEntity> client = clientRepository.findByEmail(resetPasswordRequest.getEmailId());
		client.get().setPassword(BusinessUtility.cryptWithMD5(resetPasswordRequest.getPassword()));
		client.get().setIsForgotPasswordStatus(false);
		logger.info("Exiting from updatePassword() of Client BusinessDeleg");
		return new ClientResponse(mapper.clientToClientModel(clientRepository.save(client.get())), null, HttpStatus.OK);
	}
}
