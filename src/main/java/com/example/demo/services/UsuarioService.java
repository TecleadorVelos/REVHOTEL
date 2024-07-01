package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.classes.Usuario;


@Service
public class UsuarioService {
	
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
