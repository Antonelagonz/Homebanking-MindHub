package com.mindhub.homebanking.configuration;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

//Indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime, for example:
@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired //inyecta una instancia del clientrepository
    ClientServices clientServices;
    @Bean // es una anotacion de spring que hace que se cree una instancia de la clase en el application context - una vez que se compila va a haber una instancia de esta clase
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    //tiene el bean porque lo quiero ocupar y reutilizar en la aplicacion

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            Client client = clientServices.findByEmail(email);
            if (client != null) {
                if (client.getEmail().contains("@admin.com")) {
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN", "CLIENT"));
                } else {
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
            }
            else {
                throw new UsernameNotFoundException("Unknown client: " + email);
            }
        });
    }
}