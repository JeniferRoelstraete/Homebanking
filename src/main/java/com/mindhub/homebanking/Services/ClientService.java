package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> findAll();

    ClientDTO getClientById(long id);

    ClientDTO findDTOByEmail(String email);

    Client findByEmail(String email);

    void save(Client client);
}
