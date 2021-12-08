package com.lesstax.request.response.model;

public class ClientPaginationWithFIlter {

	private ClientPage clientPage;
	private ClientSearchCriteria clientSearchCriteria;

	public ClientPage getClientPage() {
		return clientPage;
	}

	public void setClientPage(ClientPage clientPage) {
		this.clientPage = clientPage;
	}

	public ClientSearchCriteria getClientSearchCriteria() {
		return clientSearchCriteria;
	}

	public void setClientSearchCriteria(ClientSearchCriteria clientSearchCriteria) {
		this.clientSearchCriteria = clientSearchCriteria;
	}

}
