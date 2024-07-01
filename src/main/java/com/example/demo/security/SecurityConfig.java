package com.example.demo.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.classes.Usuario;
import com.example.demo.services.UsuarioDetailService;
import com.example.demo.services.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private UsuarioDetailService miUsuarioDetailService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(registry -> {
				registry.requestMatchers("/", "/login", "/crearUsuario", "/h2-console/**", "/images/**", "/css/**", "/js/**").permitAll();
				registry.requestMatchers("/menuAdmin","/hoteles","/promociones", "formularioPromocion", "/hotelesyhabitaciones/**", "/anadirhabitacion/**").hasRole("ADMIN");
				registry.requestMatchers("/menuUsuarios/**","/historialreservas/**","/reservas/**",
						"/nuevareserva/**", "/editarreserva/**").hasRole("USER");
				registry.anyRequest().authenticated();
			})
			.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
				.loginPage("/login")
				.loginProcessingUrl("/loginUsuarios")
                .usernameParameter("nombreUsuario")
                .passwordParameter("contrasena")
				.successHandler(authenticationSuccessHandler)
				.permitAll()
			)
			.logout((logout) -> 
			logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
			.permitAll())
			.headers().frameOptions().disable(); //Esta linea me permite visualizar los elementos de la página de la consola.

		return http.build();
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return miUsuarioDetailService;
		
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(miUsuarioDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
