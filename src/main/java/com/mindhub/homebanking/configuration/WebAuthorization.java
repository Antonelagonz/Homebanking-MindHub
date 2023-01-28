package com.mindhub.homebanking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean //esta sobreescribiendo el metodo, si el metodo ya existe se sobreescribe
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()  //Es un método de http security
                .antMatchers("/web/index.html", "/web/register.html", "/web/login.html", "/web/assets/indexStyle.css", "/web/assets/registerStyle.css", "/web/assets/style.css").permitAll() //todos van a poder acceder a estas paginas
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/clients").permitAll()
                .antMatchers("/web/accounts.html", "/web/cards.html", "/web/account.html", "/web/assets/style.css", "/web/transfers.html", "/web/create-cards.html", "/web/loan-application.html").hasAuthority("CLIENT")
                .antMatchers("/rest/**", "/web/manager.html", "/api/loans/create").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients", "/web/assets/style.css").hasAuthority("ADMIN");//le asigna a que puede acceder cada cliente con su rol
                //antMatchers es una clase de Spring que se usa para comparar urls con solicitudes y autorizarlas.
        http.formLogin()  //es el formulario de logeo para la aplicacion
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        //se desactivan porque estamos haciendo peticiones a la api y cada vez que son solicitados genera tokens
        http.csrf().disable();

        // deshabilita frameOptions para que se pueda acceder a h2-console
        http.headers().frameOptions().disable();

        // si el usuario no está autenticado , envíe una respuesta de falla de autenticación
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el inicio de sesión es exitoso, borra las banderas que solicitan autenticación
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // si falla el inicio de sesión, envía una respuesta de falla de autenticación
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //  si el cierre de sesión es exitoso, envía una respuesta exitosa
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }


    //esto es boilerplate code, se utiliza para remover la id cookie en cada logout
    private void clearAuthenticationAttributes(HttpServletRequest request) { //remueve su session id cada vez que se desloguea el cliente

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}

