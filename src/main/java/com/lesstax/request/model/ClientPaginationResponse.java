package com.lesstax.request.model;

import java.util.List;

import com.lesstax.model.Client;

public class ClientPaginationResponse {

	private List<Client> client;
	private Integer totalNumberOfPages;
	private Integer currentPage;
	private Integer pageSize;

	public ClientPaginationResponse() {
	}

	public ClientPaginationResponse(List<Client> client, Integer totalNumberOfPages, Integer currentPage, Integer pageSize) {
		super();
		this.client = client;
		this.totalNumberOfPages = totalNumberOfPages;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public List<Client> getClient() {
		return client;
	}

	public void setClient(List<Client> client) {
		this.client = client;
	}

	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
