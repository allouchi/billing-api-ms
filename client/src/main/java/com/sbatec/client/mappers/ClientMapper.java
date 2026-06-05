package com.sbatec.client.mappers;

import com.sbatec.client.dtos.Adresse;
import com.sbatec.client.dtos.Client;
import com.sbatec.client.dtos.EmailClient;
import com.sbatec.client.models.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {Adresse.class, EmailClient.class})
public interface ClientMapper {

    @Mapping(target = "adresseClient", source = "adresseClient")
    Client toDto(ClientEntity clientEntity);

    ClientEntity toEntity(Client client);

    List<Client> toDtoList(List<ClientEntity> entities);

    List<ClientEntity> toEntityList(List<Client> dtos);
}
