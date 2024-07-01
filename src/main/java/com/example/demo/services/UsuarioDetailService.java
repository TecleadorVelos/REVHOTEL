package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;

/* Esta clase será el servicio que una Spring Security con nuestra DDBB*/

@Service
public class UsuarioDetailService implements UserDetailsService{

	@Autowired
	private UsuarioRepository repositorioUsuarios;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<Usuario> optional = repositorioUsuarios.findByUsername(username);
		
		if (optional.isPresent()) {
			Usuario objUsuario = optional.get();
			String rol ="";
			if (objUsuario.getRole() == null) {
				rol = "USER";
			} else {
				rol = objUsuario.getRole();
			}
			return User.builder()
					.username(objUsuario.getUsername())
					.password(objUsuario.getContraseña())
					.roles(rol)
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
		
	}
	
	public Long returnId(String nombre) {
		
		Optional<Usuario> optional =  repositorioUsuarios.findByUsername(nombre);
		if (optional.isPresent()) {
			return optional.get().getId();
		} else {
			return null;
		}
	}
}
