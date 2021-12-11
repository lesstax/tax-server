package com.lesstax.service;

import com.lesstax.exception.BusinessException;
import com.lesstax.model.ClientFilterModel;
import com.lesstax.model.ClientModel;
import com.lesstax.model.ClientResponseModel;
import com.lesstax.model.OTPModel;


public interface ClientBusinessService {
    public ClientModel client(ClientModel clientModel);

    public ClientModel findClient(Long id) throws BusinessException, Exception;

    public ClientModel signIn(String email, String password);

    public ClientResponseModel getAllClients(ClientFilterModel clientFilterModel);

    public ClientModel verifyOtp(OTPModel otpModel) throws BusinessException;

    public ClientModel emailVerified(ClientModel clientModel);

    public Boolean removeClient(Long id);

    public ClientModel findClientByEmail(String clientEmailId);

    public ClientModel resetPassword(ClientModel clientModel);

    public ClientModel forgotPassword(String emailId) throws BusinessException;
}
