package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientServices {

    List<ClientDTO> getAllClients();

    ClientDTO getClientDTO(@PathVariable Long id);

    Client findByEmail(String email);

    void saveClient(Client client);


}
