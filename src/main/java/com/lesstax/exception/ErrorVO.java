package com.lesstax.exception;

public class ErrorVO {
	private String errorCode = null;
	private String errorDescription = null;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public ErrorVO(String errorCode, String errorDescription) {
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
}