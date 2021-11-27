package com.lesstax.mapper;

import org.mapstruct.Mapper;

import com.lesstax.model.Client;
import com.lesstax.model.mapper.ClientModel;

@Mapper
public interface ClientMapper {
	
	 ClientModel clientToClientModel(Client client);
	 Client clientModelToClient(ClientModel clientMapper);

}
