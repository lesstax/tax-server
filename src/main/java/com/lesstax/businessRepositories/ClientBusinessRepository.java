package com.lesstax.businessRepositories;

import com.lesstax.exception.BusinessException;
import com.lesstax.model.mapper.ClientModel;
import com.lesstax.request.response.model.ClientResponse;

public interface ClientBusinessRepository {

	public ClientResponse saveClient(ClientModel clientModel);

	public ClientResponse findClientById(Long id) throws BusinessException, Exception;

	public ClientResponse getAllClient();
	
	public ClientResponse clientLogin(String email, String password);
	
	public ClientResponse getAllClients(Integer pageNo, Integer pageSize) ;

	public ClientResponse updateClient(ClientModel clientModel);

	public ClientResponse emailVerified(ClientModel clientModel);

	public Boolean removeClient(Long id);

	public ClientModel findClientByEmail(String clientEmailId);
	
}
