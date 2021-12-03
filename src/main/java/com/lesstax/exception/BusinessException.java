package com.lesstax.exception;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class BusinessException extends RuntimeException {

	private List<ErrorVO> errorVO = null;

	public BusinessException() {

	}

	public BusinessException(List<ErrorVO> errorVO) {
		this.errorVO = errorVO;
	}

	public List<ErrorVO> getErrorVO() {
		return errorVO;
	}

	public void setErrorVO(List<ErrorVO> errorVO) {
		this.errorVO = errorVO;
	}

}
