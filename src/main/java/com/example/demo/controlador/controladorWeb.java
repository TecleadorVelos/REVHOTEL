package com.example.demo.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.HabitacionRepository;
import com.example.demo.classes.Hotel;
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
	
	//CONTROLADOR LOGIN USUARIOS
	
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
				model.addAttribute("message", "Contraseña Incorrecta, vuelva a intentar");
	            return "error";
			}
            
        } else {
        	model.addAttribute("message", "Error en el login del Usuario");
            return "error";
        }
    }
	
	//CONTROLADOR LOGIN ADMIN
	
	@GetMapping("/loginAdmin")
	public String loginAdministrador(Model model) {
		return "loginAdmin";
	}
	@PostMapping("/loginAdmin")
	public String procesarLoginAdministrador(Model model, @RequestParam String nombreAdmin, @RequestParam String contrasena) {
		
		Optional<Usuario> optional = repositorioUsuarios.findByUsername(nombreAdmin);
		
		if (optional.isPresent()) {
			Usuario user = optional.get();
			
			if (contrasena.equals(user.getContraseña())) {
				return "redirect:/menuAdmin";
			}else {
				model.addAttribute("message", "Contraseña Incorrecta, acceso denegado." + contrasena +"vs"+ user.getContraseña());
	            return "error";
			}
            
        } else {
        	model.addAttribute("message", "Acceso denegado.");
            return "error";
        }	
	}
	
	//CONTROLADOR MENU USUARIOS
	@GetMapping("/menuUsuarios/{id}")
	public String menuUsuarios(Model model, @PathVariable Long id) {
		return "menuUsuarios";
	}
	
	//CONTROLADOR MENU ADMIN
	@GetMapping("/menuAdmin")
	public String menuAdmin(Model model) {
		return "menuAdmin";
	}
	
	//CONTROLADOR GESTION DE HOTELES Y HABITACIONES
	@GetMapping("/hotelesyhabitaciones")
	public String hotelesyHabitaciones(Model model) {
		List<Hotel> hoteles = repositorioHotel.findAll();
		model.addAttribute("hoteles", hoteles);
		
		return "gestionHoteles";
	}
	
	@GetMapping("/hotelesyhabitaciones/{id}")
	public String hotelesyHabitaciones(Model model, @PathVariable Long id) {
		
		Optional<Hotel> optional = repositorioHotel.findById(id);
		
		if (optional.isPresent()) {
			Hotel hotel = optional.get();
			model.addAttribute("id", hotel.getHotelId());
			model.addAttribute("nombre",hotel.getNombre());
			model.addAttribute("numHabitaciones",hotel.getNumHabitaciones());
			model.addAttribute("habitaciones", hotel.getHabitaciones());
			
			return "gestionHabitaciones";
		}
		else {
			model.addAttribute("message", "Se ha producido un error, hotel no encontrado.");
			return "error";
		}
		
	}
	
}
