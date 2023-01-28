package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//repasar clase 23/11 13:45
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);

//clientrepository hereda los metodos de JpaRepository
}

