package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repositorioUsuarios;
	
	public UsuarioService() {}
	
	public boolean nombreValido(String nombre, List<Usuario> usuarios){
		boolean esNombreValido = true;
		for (Usuario u: usuarios) {
			if (u.getUsername().equals(nombre)) {
				esNombreValido = false;
				break;
			}	
		}
		return esNombreValido;
	}
	
	
}
