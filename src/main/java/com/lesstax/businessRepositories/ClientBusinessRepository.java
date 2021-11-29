package com.lesstax.businessRepositories;

import java.util.List;

import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;

public interface ClientBusinessRepository {

	public Client saveClient(Client client);

	public ClientModel findClient(Long id) throws Exception;

	public List<Client> getAllClient();
	
	public ClientModel clientLogin(String email, String password);
	
	public List<Client> getAllClients(Integer pageNo, Integer pageSize) ;
	
}
