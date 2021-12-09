package com.lesstax.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.lesstax.model.ClientEntity;
import com.lesstax.model.mapper.ClientModel;

@Mapper
public interface ClientMapper {
	
	 ClientModel clientToClientModel(ClientEntity client);
	 ClientEntity clientModelToClient(ClientModel clientMapper);
	 List<ClientModel> clientToClientModelList(List<ClientEntity> clients);
	 List<ClientEntity> clientModelToClientList(List<ClientModel> clientModels);
}
