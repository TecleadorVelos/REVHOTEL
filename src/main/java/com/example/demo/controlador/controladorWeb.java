package com.example.demo.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class controladorWeb {

	
	@GetMapping("/")
	public String menuprincipal(Model model) {
		return "inicio";
	}
	@GetMapping("/loginUsuarios")
	public String loginUsuarios(Model model) {
		return "loginUsuarios";
	}
	@GetMapping("/loginAdmin")
	public String loginAdministrador(Model model) {
		return "loginAdmin";
	}
	
}
