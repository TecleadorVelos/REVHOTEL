package com.example.demo.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;
import com.example.demo.services.UsuarioDetailService;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	UsuarioRepository repositorioUsuarios;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
		if (isAdmin) {
			setDefaultTargetUrl("/menuAdmin");
		}else {
			
			String username = authentication.getName();
			Optional<Usuario> optional = repositorioUsuarios.findByUsername(username);
			if (optional.isPresent()) {
				Long id = optional.get().getId();
				setDefaultTargetUrl("/menuUsuarios/" + id);
			}else {
				setDefaultTargetUrl("/errorInicioSesion");
			}
			
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
