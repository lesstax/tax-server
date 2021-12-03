package com.lesstax.businessRepositories;

import java.util.List;

import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.request.model.ClientPaginationResponse;

public interface ClientBusinessRepository {

	public Client saveClient(Client client);

	public ClientModel findClient(Long id) throws Exception;

	public List<Client> getAllClient();
	
	public ClientModel clientLogin(String email, String password);
	
	public ClientPaginationResponse getAllClients(Integer pageNo, Integer pageSize) ;

	public Client updateClient(Client client);

	public Client emailVerified(Client client);
	
}
