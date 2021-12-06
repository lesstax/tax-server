package com.lesstax.model.mapper;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lesstax.model.PlanEntity;
import com.lesstax.model.RoleEntity;

public class ClientModel {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("first-name")
	private String firstName;

	@JsonProperty("middle-name")
	private String middleName;

	@JsonProperty("last-name")
	private String lastName;

	@JsonProperty("mobile-number")
	private String mobileNumber;

	@JsonProperty("email")
	private String email;

	@JsonProperty("password")
	private String password;

	@JsonProperty("is-email-verified")
	private Boolean isEmailVerified;

	@JsonProperty("expiry-date")
	private LocalDateTime expiryDate;
	
	@JsonProperty("role")
	private RoleEntity role;

	@JsonProperty("plan")
	private PlanEntity plan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

}
