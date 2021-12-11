package com.lesstax.controller;

import com.lesstax.exception.BusinessException;
import com.lesstax.model.ClientFilterModel;
import com.lesstax.model.ClientModel;
import com.lesstax.model.ClientResponseModel;
import com.lesstax.model.OTPModel;
import com.lesstax.service.ClientBusinessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

    private static Logger logger = LogManager.getLogger(ClientController.class);

    @Autowired
    private ClientBusinessService clientBusinessService;

    @PostMapping("/client")
    public ClientModel client(@RequestBody ClientModel clientModel) {
        logger.info("Inside signUp() of client controller");
        logger.info("Exiting from signUp() of client controller");
        return clientBusinessService.client(clientModel);
    }

    @GetMapping("/{id}")
    public ClientModel findClient(@PathVariable Long id) throws Exception {
        logger.info("Inside findClient() of client controller");
        logger.info("Exiting from findClient() of client controller");
        return clientBusinessService.findClient(id);
    }

    @PostMapping("/sign-in")
    public ClientModel signIn(@RequestBody ClientModel clientModel) throws BusinessException {
        logger.info("Inside signIn() of client controller");
        logger.info("Exiting from signIn() of client controller");
        return clientBusinessService.signIn(clientModel.getEmail(), clientModel.getPassword());
    }

    @PostMapping("/clients")
    public ClientResponseModel getAllClients(@RequestBody ClientFilterModel clientFilterModel) {
        logger.info("Inside getClientsWithPagination() of client controller");
        logger.info("Exiting from getClientsWithPagination() of client controller");
        return clientBusinessService.getAllClients(clientFilterModel);
    }

    @PostMapping("/verify-otp")
    public ClientModel verifyOtp(@RequestBody OTPModel otpModel) throws BusinessException {
        logger.info("Inside verifyOtp() of client controller");
        return clientBusinessService.verifyOtp(otpModel);
    }

    @GetMapping("/remove-client/{id}")
    public Boolean removeClient(@PathVariable Long id) {
        logger.info("Inside removeClient() of client controller");
        logger.info("Exiting from removeClient() of client controller");
        return clientBusinessService.removeClient(id);
    }

    @PostMapping("/reset-password")
    public ClientModel resetPassword(@RequestBody ClientModel clientModel) {
        logger.info("Inside resetPassword() of client controller");
        logger.info("Exiting from resetPassword() of client controller");
        return clientBusinessService.resetPassword(clientModel);
    }

    @GetMapping("/forgot-password")
    public ClientModel forgotPassword(@RequestParam String emailId) throws BusinessException {
        logger.info("Inside updateClient() of client controller");
        logger.info("Exiting from updateClient() of client controller");
        return clientBusinessService.forgotPassword(emailId);
    }
}
