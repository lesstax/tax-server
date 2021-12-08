package com.lesstax.request.response.model;

import org.springframework.http.HttpStatus;

import com.lesstax.exception.BusinessException;
import com.lesstax.model.mapper.ClientModel;

public class ClientResponse {

	private Object clientModel;
	private HttpStatus httpStatus;
	private BusinessException businessException;

	public ClientResponse() {

	}

	public ClientResponse(Object clientModel, BusinessException businessException, HttpStatus httpStatus) {
		super();
		this.clientModel = clientModel;
		this.httpStatus = httpStatus;
		this.businessException = businessException;
	}

	public BusinessException getBusinessException() {
		return businessException;
	}

	public void setBusinessException(BusinessException businessException) {
		this.businessException = businessException;
	}

	public Object getClientModel() {
		return clientModel;
	}

	public void setClientModel(Object clientModel) {
		this.clientModel = clientModel;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
