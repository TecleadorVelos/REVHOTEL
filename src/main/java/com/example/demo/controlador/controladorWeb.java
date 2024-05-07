package com.example.demo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.classes.HabitacionRepository;
import com.example.demo.classes.HotelRepository;
import com.example.demo.classes.ReservaRepository;
import com.example.demo.classes.UsuarioRepository;


@Controller
public class controladorWeb {
	
	@Autowired
	private HotelRepository repositorioHotel;
	
	@Autowired
	private HabitacionRepository repositorioHabitacion;
	
	@Autowired
	private ReservaRepository repositorioReservas;
	
	@Autowired
	private UsuarioRepository reposiotrioUsuarios;
	
	
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
	@GetMapping("/menuUsuarios")
	public String menuUsuarios(Model model) {
		return "menuUsuarios";
	}
}
