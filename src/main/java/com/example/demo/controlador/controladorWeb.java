package com.example.demo.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.HabitacionRepository;
import com.example.demo.classes.HotelRepository;
import com.example.demo.classes.ReservaRepository;
import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;

import jakarta.servlet.http.HttpSession;


@Controller
public class controladorWeb {
	
	@Autowired
	private HotelRepository repositorioHotel;
	
	@Autowired
	private HabitacionRepository repositorioHabitacion;
	
	@Autowired
	private ReservaRepository repositorioReservas;
	
	@Autowired
	private UsuarioRepository repositorioUsuarios;
	
	
	@GetMapping("/")
	public String menuprincipal(Model model) {
		return "inicio";
	}
	@GetMapping("/loginUsuarios")
	public String loginUsuarios(Model model) {
		return "loginUsuarios";
	
	}
	@PostMapping("/loginUsuarios")
    public String procesarLogin(Model model, @RequestParam String nombreUsuario, @RequestParam String contrasena) {
        
		Optional<Usuario> optional = repositorioUsuarios.findByUsername(nombreUsuario);
		
		if (optional.isPresent()) {
			Usuario user = optional.get();
			Long id = user.getId();
			
			if (contrasena == user.getContraseña()) {
				return "redirect:/menuUsuarios/" + id ;
			}else {
				model.addAttribute("message", "Contraseña Incorrecta");
	            return "error";
			}
            
        } else {
        	model.addAttribute("message", "Error en el login del Usuario");
            return "error";
        }
    }
	@GetMapping("/loginAdmin")
	public String loginAdministrador(Model model) {
		return "loginAdmin";
	}
	
	@GetMapping("/menuUsuarios/{id}")
	public String menuUsuarios(Model model, @PathVariable Long id) {
		return "menuUsuarios";
	}
}
