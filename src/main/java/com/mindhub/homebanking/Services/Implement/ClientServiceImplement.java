package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(long id) {
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @Override
    public ClientDTO findDTOByEmail(String email) {
        return new ClientDTO(clientRepository.findByEmail(email));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }
}
