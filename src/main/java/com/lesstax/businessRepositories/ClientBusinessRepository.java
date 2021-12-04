package com.lesstax.businessRepositories;

import java.util.List;

import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.request.model.ClientPaginationResponse;

public interface ClientBusinessRepository {

	public ClientModel saveClient(ClientModel clientModel);

	public ClientModel findClient(Long id) throws Exception;

	public List<ClientModel> getAllClient();
	
	public ClientModel clientLogin(String email, String password);
	
	public ClientPaginationResponse getAllClients(Integer pageNo, Integer pageSize) ;

	public ClientModel updateClient(ClientModel clientModel);

	public ClientModel emailVerified(ClientModel clientModel);

	public Boolean removeClient(Long id);

	
}
