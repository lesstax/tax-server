package com.lesstax.mapper;

import com.lesstax.entity.ClientEntity;
import com.lesstax.model.ClientModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientModel clientToClientModel(ClientEntity client);

    ClientEntity clientModelToClient(ClientModel clientMapper);
    
    List<ClientEntity> clientModelToClientList(List<ClientModel> clientModels);

    List<ClientModel> constructClientModels(List<ClientEntity> clientModels);
}
