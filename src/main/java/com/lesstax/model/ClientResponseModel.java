package com.lesstax.model;

import java.util.List;

public class ClientResponseModel {
    private List<ClientModel> clients;
    private Integer totalNumberOfPages;
    private Integer currentPage;
    private Integer pageSize;

    public ClientResponseModel() {
    }

    public ClientResponseModel(List<ClientModel> clients, Integer totalNumberOfPages, Integer currentPage, Integer pageSize) {
        super();
        this.clients = clients;
        this.totalNumberOfPages = totalNumberOfPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public List<ClientModel> getClients() {
        return clients;
    }

    public void setClients(List<ClientModel> clients) {
        this.clients = clients;
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
