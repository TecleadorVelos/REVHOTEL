package com.example.demo.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.Habitacion;
import com.example.demo.classes.HabitacionRepository;
import com.example.demo.classes.Hotel;
import com.example.demo.classes.HotelRepository;
import com.example.demo.classes.Promocion;
import com.example.demo.classes.PromocionesRepository;
import com.example.demo.classes.ReservaRepository;
import com.example.demo.classes.THabitacion;
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
	
	@Autowired
	private PromocionesRepository repositorioPromociones;
	
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
			
			if (contrasena.equals(user.getContraseña())) {
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
				model.addAttribute("message", "Contraseña Incorrecta, acceso denegado.");
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
		
		Optional<Usuario> optional = repositorioUsuarios.findById(id);
		if (optional.isPresent()) {
			Usuario user = optional.get();
			
			model.addAttribute("nombre", user.getUsername());
			model.addAttribute("numeroPuntos", user.getPuntos());
			model.addAttribute("numeroReservas", user.getNumReservas());
			model.addAttribute("Promociones", repositorioPromociones.findAll());
			
			return "menuUsuarios";
		}
		else {
			model.addAttribute("message", "Error, Usuario no encontrado.");
            return "error";
		}
		
	}
	
	//CONTROLADOR MENU ADMIN
	@GetMapping("/menuAdmin")
	public String menuAdmin(Model model) {
		return "menuAdmin";
	}
	//CONTROLADOR DE PROMOCIONES
	@GetMapping("/promociones")
	public String promociones(Model model) {
		List<Promocion> promociones = repositorioPromociones.findAll();
		model.addAttribute("promociones", promociones);
		
		return "gestionPromociones";
	}
	
	@GetMapping("/formularioPromocion")
	public String formularioPromocion(Model model) {
		
		return "formularioPromocion";
	}
	@PostMapping("/crearpromocion")
	public String crearpromocion(Model model, @RequestParam Double oferta , @RequestParam String fechainicio, @RequestParam String fechafinal) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		try {
		    LocalDate dateinicio = LocalDate.parse(fechainicio, formatter);
		    LocalDate datefinal = LocalDate.parse(fechafinal, formatter);
		    
		    Promocion nuevapromo = new Promocion (oferta,dateinicio,datefinal);
		    repositorioPromociones.save(nuevapromo);
		
		    return "redirect:/promociones";
		    
		} catch (DateTimeParseException e) {
			model.addAttribute("message", "Error, en la conversión de fechas.");
            return "error";
		}

	}
	@GetMapping("/borrarpromocion/{id}")
	public String borrarpromocion(Model model, @PathVariable Long id) {
		
		Optional<Promocion> optional = repositorioPromociones.findById(id);
		
		if (optional.isPresent()) {
			repositorioPromociones.deleteById(id);
			return "redirect:/promociones";
		}
		else {
			model.addAttribute("message", "Se ha producido un error, promoción no encontrada.");
			return "error";
		}
		
	}
	//CONTROLADOR GESTION DE HOTELES
	
	@GetMapping("/hoteles")
	public String hotelesyHabitaciones(Model model) {
		List<Hotel> hoteles = repositorioHotel.findAll();
		model.addAttribute("hoteles", hoteles);
		
		return "gestionHoteles";
	}
	@PostMapping("/borrarhotel")
	public String borrarhotel(Model model, @RequestParam Long id) {
		
		Optional<Hotel> optional = repositorioHotel.findById(id);
		if (optional.isPresent()) {
			repositorioHotel.deleteById(id);
			return "redirect:/hoteles";
		}
		else {
			model.addAttribute("message", "Se ha producido un error, hotel no encontrado.");
			return "error";
		}
		
	}
	
	@PostMapping("/crearhotel")
	public String crearhotel(Model model, @RequestParam String nombrehotel) {
		Hotel nuevoHotel = new Hotel (nombrehotel);
		repositorioHotel.save(nuevoHotel);
		
		return "redirect:/hoteles";
	}
	//CONTROLADOR GESTION DE HABITACIONES
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
	@GetMapping("/anadirhabitacion/{id}")
	public String anadirhabitacion(Model model, @PathVariable Long id) {
		model.addAttribute("identificadorHotel", id);
		return "formularioNuevaHabitacion" ;
	}
	
	@PostMapping("/anadirhabitacion/{id}")
	public String anadirhabitacion(Model model, @PathVariable Long id, @RequestParam String numeroHabitacion, @RequestParam String tipoHabitacion) {
		
		Optional<Hotel> optional = repositorioHotel.findById(id);

		if (optional.isPresent()) {
			Hotel hotel = optional.get();
			THabitacion tipo = null;
			switch(tipoHabitacion){
			case "Normal": tipo = THabitacion.NORMAL;
				break;
			case "Doble": tipo = THabitacion.DOBLE;
				break;
			case "Suite": tipo = THabitacion.SUITE;
				break;
			}
			
			Habitacion habitacionnueva = new Habitacion(hotel,numeroHabitacion, tipo);
			repositorioHabitacion.save(habitacionnueva);
			hotel.anadirHabitacion(habitacionnueva);
			repositorioHotel.save(hotel);
			return "redirect:/hotelesyhabitaciones/" + id;
			
		}
		else {
			model.addAttribute("message", "Error, hotel no encontrado");
			return "error";
		}
		
	}
	@GetMapping("/borrarhabitacion/{id}/{habitacionId}")
	public String borrarhabitacion(Model model, @PathVariable Long id, @PathVariable Long habitacionId) {
		
		Optional<Hotel> optional = repositorioHotel.findById(id);
		
		if (optional.isPresent()) {
			Optional<Habitacion> opthabitacion = repositorioHabitacion.findById(habitacionId);
			if (opthabitacion.isPresent()) {
				repositorioHabitacion.deleteById(habitacionId);
				return "redirect:/hotelesyhabitaciones/" + id;
			}
			else {
				model.addAttribute("message", "Error, habitación no encontrada");
				return "error";
			}
		}
		else {
			model.addAttribute("message", "Error, hotel no encontrado");
			return "error";
		}
	}
	// CONTROLADOR PARTE USUARIOS
}
