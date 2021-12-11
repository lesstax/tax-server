package com.lesstax.exception;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorCreatorService {
    private static Map<String, String> errorMap = null;

    static {
        errorMap = new HashMap<>();
        errorMap.put(ExceptionsKeys.CLIENT_NOT_FOUND, "Clients not found");
        errorMap.put(ExceptionsKeys.CLIENT_EMAIL_FOUND, "Client Already found with this email id");
        errorMap.put(ExceptionsKeys.CLIENT_CREDENTIALS_INVALID, "Your email id or password is incorrect");
        errorMap.put(ExceptionsKeys.CLIENT_OTP_INCORRECT, "Your OTP is incorrect");
        errorMap.put(ExceptionsKeys.CLIENT_EMAIL_NOT_FOUND, "No Client found with this email id");
        errorMap.put(ExceptionsKeys.CLIENT_NO_RECORDS, "No records found");
    }

    public ErrorVO constructError(String errorKey) {
        if (errorMap.containsKey(errorKey)) return new ErrorVO(errorKey, errorMap.get(errorKey));
        else return null;
    }

    public BusinessException businessException(List<ErrorVO> errors) {
        return new BusinessException(errors);
    }

    public BusinessException businessException(String errorKey) {
        ErrorVO errorVO = errorMap.containsKey(errorKey) ? new ErrorVO(errorKey, errorMap.get(errorKey)) : new ErrorVO("00", "Exception not found");
        return new BusinessException(Arrays.asList(errorVO));
    }
}
