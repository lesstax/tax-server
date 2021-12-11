package com.lesstax.service.impl;

import com.lesstax.entity.ClientEntity;
import com.lesstax.exception.BusinessException;
import com.lesstax.exception.ErrorCreatorService;
import com.lesstax.exception.ExceptionsKeys;
import com.lesstax.mapper.ClientMapper;
import com.lesstax.model.ClientFilterModel;
import com.lesstax.model.ClientModel;
import com.lesstax.model.ClientResponseModel;
import com.lesstax.model.OTPModel;
import com.lesstax.repositories.ClientRepository;
import com.lesstax.service.ClientBusinessService;
import com.lesstax.service.SendEmailService;
import com.lesstax.service.SendOTPService;
import com.lesstax.utility.BusinessUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {
    private static final Logger logger = LogManager.getLogger(ClientBusinessServiceImpl.class);
    @Autowired
    private ClientMapper mapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SendEmailService sendEmail;

    @Autowired
    private ErrorCreatorService errorCreatorService;

    @Autowired
    private SendOTPService sendOTPService;

    @Override
    public ClientModel client(ClientModel clientModel) {
        logger.info("Inside saveClient() of Client BusinessDelegate");
        if (clientRepository.findByEmail(clientModel.getEmail()).isPresent()) {
            throw errorCreatorService.businessException(ExceptionsKeys.CLIENT_EMAIL_FOUND);
        } else {
            Optional<ClientEntity> existingUser = clientRepository.findById(clientModel.getId());
            ClientEntity client = existingUser.isPresent() ?
                    existingUser.get() : mapper.clientModelToClient(clientModel);
            client.setCreatedBy(clientModel.getFirstName());
            client.setPassword(BusinessUtility.cryptWithMD5(client.getPassword()));
            client = clientRepository.save(client);
            sendEmail.sendOTPOnMail(client.getEmail(), client.getFirstName(), "OTP");
            return mapper.clientToClientModel(client);
        }
    }

    @Override
    public ClientModel findClient(Long id) throws BusinessException {
        logger.info("Inside findClient() of Client BusinessDelegate");
        ClientEntity client = clientRepository.findById(id).orElseThrow(() -> errorCreatorService.businessException(ExceptionsKeys.CLIENT_NOT_FOUND));
        logger.info("Exiting from findClient() of Client BusinessDelegate");
        return mapper.clientToClientModel(client);
    }

    @Override
    public ClientModel signIn(String email, String password) {
        logger.info("Inside clientLogin() of Client BusinessDelegate");
        Optional<ClientEntity> client = clientRepository.findByEmail(email);
        password = BusinessUtility.cryptWithMD5(password);
        if (client.isPresent() && client.get().getPassword().equals(password)) {
            logger.info("Exiting from clientLogin() of Client BusinessDelegate");
            return mapper.clientToClientModel(client.get());
        } else {
            throw errorCreatorService.businessException(ExceptionsKeys.CLIENT_CREDENTIALS_INVALID);
        }
    }

    @Override
    public ClientResponseModel getAllClients(ClientFilterModel filterModel) throws BusinessException {
        Pageable paging = PageRequest.of(filterModel.getPageNo(), filterModel.getPageSize());
        Page<ClientEntity> pagedResult = clientRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            logger.info("Exiting from getAllClients() of Client BusinessDelegate");
            return new ClientResponseModel(mapper.constructClientModels(pagedResult.getContent()),
                    pagedResult.getTotalPages(), pagedResult.getNumber(), pagedResult.getSize());
        } else {
            throw errorCreatorService.businessException(ExceptionsKeys.CLIENT_NO_RECORDS);
        }
    }

    @Override
    public ClientModel verifyOtp(OTPModel otpModel) throws BusinessException {
        logger.info("Inside verifyOtp() of client controller");
        Integer storedOtp = sendOTPService.getOtp(otpModel.getClientEmail());
        if (otpModel.getOtp().intValue() == storedOtp.intValue()) {
            logger.info("Inside emailVerified() of Client BusinessDelegate");
            ClientEntity client = clientRepository.findByEmail(otpModel.getClientEmail()).get();
            client.setIsEmailVerified(true);
            client.setUpdatedBy(client.getFirstName());
            logger.info("Exiting from emailVerified() of Client BusinessDelegate");
            return mapper.clientToClientModel(clientRepository.save(client));
        } else {
            throw errorCreatorService.businessException(ExceptionsKeys.CLIENT_OTP_INCORRECT);
        }
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
    public Boolean removeClient(Long id) {
        logger.info("Inside removeClient() of Client BusinessDelegate");
        clientRepository.deleteById(id);
        logger.info("Exiting from removeClient() of Client BusinessDeleg");
        return true;
    }

    @Override
    public ClientModel findClientByEmail(String clientEmailId) {
        logger.info("Inside findClientByEmail() of Client BusinessDelegate");
        logger.info("Exiting from findClientByEmail() of Client BusinessDelegate");
        return mapper.clientToClientModel(clientRepository.findByEmail(clientEmailId).get());
    }

    @Override
    public ClientModel resetPassword(ClientModel clientModel) {
        logger.info("Inside updatePassword() of Client BusinessDelegate");
        Optional<ClientEntity> client = clientRepository.findByEmail(clientModel.getEmail());
        client.get().setPassword(BusinessUtility.cryptWithMD5(clientModel.getPassword()));
        client.get().setIsForgotPasswordStatus(false);
        logger.info("Exiting from updatePassword() of Client BusinessDeleg");
        return mapper.clientToClientModel(clientRepository.save(client.get()));
    }

    @Override
    public ClientModel forgotPassword(String clientEmailId) throws BusinessException {
        logger.info("Inside sendTemporaryPassword() of Client BusinessDelegate");
        Optional<ClientEntity> client = clientRepository.findByEmail(clientEmailId);
        client.orElseThrow(() -> errorCreatorService.businessException(ExceptionsKeys.CLIENT_EMAIL_NOT_FOUND));
        sendEmail.sendOTPOnMail(clientEmailId, null, "password");
        Integer storedOtp = sendOTPService.getOtp(clientEmailId);
        String password = Integer.toString(storedOtp);
        client.get().setPassword(BusinessUtility.cryptWithMD5(password));
        client.get().setIsForgotPasswordStatus(true);
        client = Optional.of(clientRepository.save(client.get()));
        logger.info("Exiting from sendTemporaryPassword() of Client BusinessDeleg");
        return mapper.clientToClientModel(client.get());
    }
}
