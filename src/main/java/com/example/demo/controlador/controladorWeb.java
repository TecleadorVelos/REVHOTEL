package com.example.demo.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.demo.classes.Reserva;
import com.example.demo.classes.ReservaRepository;
import com.example.demo.classes.THabitacion;
import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;
import com.example.demo.services.ReservaService;
import com.example.demo.services.UsuarioService;

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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String menuprincipal(Model model) {
		return "inicio";
	}
	
	//CONTROLADOR LOGIN USUARIOS
	
	@GetMapping("/login") //Muestra el login 
	public String loginUsuarios(Model model) {
		return "login";
	
	}
	
	@GetMapping("/crearUsuario")
	public String formularioCrearUsuario(Model model) {
		return "formularioCrearUsuario";
	}
	@PostMapping("/crearUsuario")
	public String crearUsuario(Model model, @RequestParam String nombre, @RequestParam String contrasena) {
		
		//Comprobamos que el nombre sea válido
		List<Usuario> usuarios = repositorioUsuarios.findAll();
		UsuarioService servicioUsuario = new UsuarioService();
		boolean nombrevalido = servicioUsuario.nombreValido(nombre,usuarios);
		
		if (nombrevalido) {
			String contrasenacodificada = passwordEncoder.encode(contrasena);
			Usuario nuevoUsuario = new Usuario(nombre,contrasenacodificada, LocalDate.now());
			nuevoUsuario.setRole("USER");
			repositorioUsuarios.save(nuevoUsuario);
			return "loginUsuarios";
		}else {
			model.addAttribute("message", "Error, nombre ya en uso. Pruebe con uno nuevo.");
            return "errorFormularioCrearUsuario";
		}
		
		
	}
	//CONTROLADOR LOGIN ADMIN
	
	//CONTROLADOR MENU USUARIOS
	@GetMapping("/menuUsuarios/{id}") //Muestra el menú que ve un usuario
	public String menuUsuarios(Model model, @PathVariable Long id) {
		
		Optional<Usuario> optional = repositorioUsuarios.findById(id);
		if (optional.isPresent()) {
			Usuario user = optional.get();
			
			model.addAttribute("nombre", user.getUsername());
			model.addAttribute("numeroPuntos", user.getPuntos());
			model.addAttribute("numeroReservas", user.getNumReservas());
			model.addAttribute("Promociones", repositorioPromociones.findAll());
			model.addAttribute("id", id);
			
			return "menuUsuarios";
		}
		else {
			model.addAttribute("message", "Error, Usuario no encontrado.");
            return "error";
		}
		
	}
	//CONTROLADOR DE LAS RESERVAS DE LOS USUARIOS
	
	@GetMapping("/reservas/{id}") //Muestra la pestaña de reservas de un usuario
	public String reservas(Model model, @PathVariable Long id) {
		Optional<Usuario> optionaluser = repositorioUsuarios.findById(id);
		if (optionaluser.isEmpty()) {
			model.addAttribute("message", "Error, Usuario no encontrado.");
            return "error";
		}
		
		Optional<List<Reserva>> optional = repositorioReservas.findByUsuario(optionaluser.get());
		
		if (optional.isEmpty()) {
			model.addAttribute("message", "Error, reservas no encontradas.");
            return "error";
		}
		
		model.addAttribute("reservas", optional.get());
		model.addAttribute("numReservas", optional.get().size());
		model.addAttribute("nombre", optionaluser.get().getUsername());
		model.addAttribute("idUsuario", optionaluser.get().getId());
		
		return "menuReservas";
	}
	@GetMapping("/nuevareserva/{id}") //Muestra el formulario para realizar una nueva reserva
	public String crearreserva(Model model,@PathVariable Long id) {
		
		List<Hotel> hoteles = repositorioHotel.findAll();
		model.addAttribute("id", id);
		model.addAttribute("hoteles", hoteles);
		return "formularioNuevaReserva";
		
	}
	@PostMapping("/nuevareserva/{id}") //POST para realizar una nueva reserva
	public String crearNuevaReserva(Model model,@PathVariable Long id, @RequestParam String selectorHotel, @RequestParam String tipoHabitacion, 
			@RequestParam String fechaEntrada, @RequestParam String fechaSalida) {
		
		
		/* *ALGORITMO*
		 * 1º- Comprobamos que el formato de las fechas sea correcto.
		 * 2º- Obtenemos el THabitacion.
		 * 3º- Buscamos el hotel.
		 * 4º- Obtenemos todas las habitaciones del hotel.
		 * 5º- Nos quedamos con las habitaciones del tipo deseado
		 * 6º- Comprobamos si podemos realizar la reserva en base a las fechas introducidas.
		 * 6.1º- Cogemos una habitacion y sacamos todas sus reservas.
		 * 6.2º- Si no se puede reservar en la habitacion pq ya hay una reserva en esas fechas se salta a la siguiente hab
		 * 6.3º- Al acabar las reservas de la habitacion, si reservarokhab esta a true se realiza la reserva 
		 * 6.4º - sino se pasa a la siguiente habitacion.*/
		
		
		//Comprobamos que el formato de las fechas sea el correcto
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	    LocalDate fechaInicioReserva2 = LocalDate.parse(fechaEntrada, formatter);

	    LocalDate fechaFinReserva2 = LocalDate.parse(fechaSalida, formatter);
		
		//Convertimos el tipohabitacion a THabitacion
		THabitacion tipo = null;
		switch(tipoHabitacion){
		case "Normal": tipo = THabitacion.NORMAL;
			break;
		case "Doble": tipo = THabitacion.DOBLE;
			break;
		case "Suite": tipo = THabitacion.SUITE;
			break;
		}
		// Buscamos el hotel
		Optional<Hotel> optionalhotel = repositorioHotel.findByNombre(selectorHotel);
		if(optionalhotel.isEmpty()){
			model.addAttribute("message", "Error, hotel no encontrado.");
            return "errorFormularioNuevaReserva";
		}else {
			//Buscamos las habitaciones
		Optional<List<Habitacion>> optionalHabitaciones = repositorioHabitacion.findByHotel(optionalhotel.get());
			if (optionalHabitaciones.isEmpty()) {
				model.addAttribute("message", "Error, habitaciones no encontradas.");
	            return "errorFormularioNuevaReserva";
			}else {
				boolean haytipo = false;
				List<Habitacion> listaHabitaciones = optionalHabitaciones.get();
				ArrayList<Habitacion> habitacionestipo = new ArrayList<>();
				for (Habitacion hab: listaHabitaciones) {
					if (hab.getTipohabitacion().equals(tipo)) {
						habitacionestipo.add(hab);
						haytipo = true;
					}
				}
				//Si llegamos aqui hay que comprobar el boolean
				if (haytipo != true) { // no hay habitaciones
					model.addAttribute("message", "Error, no existen habitaciones en este hotel del tipo deseado.");
		            return "errorFormularioNuevaReserva";
				}
				else {
					// Existen habitaciones y hay que comprobar las fechas
					boolean reservarokhotel = false;
					for (Habitacion habi: habitacionestipo) { //iteramos las posibles habitaciones
						List<Reserva> reservashab = habi.getReservas();
						boolean reservarokhab = true; //este boolean me marca si puedo reservar en una habitacion tras haber comprobado todas las reservas de la misma
						for (Reserva posiblereserva: reservashab) { //iteramos las reservas de la habitacion
							ReservaService servicioReservas = new ReservaService();
							if (servicioReservas.reservaComparator(posiblereserva, fechaInicioReserva2, fechaFinReserva2) == false) {
								//no se puede , ya que hay una reserva
								reservarokhab = false;
								
								break;	
							}	
						}
						if (reservarokhab == true) {
							// realizamos la reserva
							/* 1º - Creamos la Reserva
							 * 2º- La añadimos a la Habitacion
							 * 3º - Guardamos en el repositorio de habitaciones
							 * 4º - Guardamos en el repositorio de reservas
							 */
							reservarokhotel = true;
							//1º PASO
							Reserva nuevareserva = new Reserva(fechaInicioReserva2,fechaFinReserva2, habi );
							// Añadimos a la reserva el hotel y el usuario
							Hotel hotel = optionalhotel.get();
							Usuario user = repositorioUsuarios.findById(id).get(); //Quizas habría que hacer un tratamiento de excepcion aqui en el caso de que no haya usuario
							nuevareserva.setUsuario(user);
							nuevareserva.setHotel(hotel);
							user.anadirPuntos(tipo);
							user.setNumReservas(user.getNumReservas() + 1);
							//2º PASO
							habi.anadirReserva(nuevareserva);
							//3º PASO
							repositorioHabitacion.save(habi);
							//4º PASO
							repositorioReservas.save(nuevareserva);
							repositorioUsuarios.save(user);
							break;
						}
						
					} // se acaba el primer for 
					if (reservarokhotel == true) {
						return "redirect:/reservas/{id}";
					}else {
						model.addAttribute("message", "Error, no es posible reservar este tipo de habitación para este hotel en las fechas seleccionadas.");
			            return "errorFormularioNuevaReserva";
					}	
				}
			}
		}
	}
	
	@GetMapping("/borrarreserva/{idUsuario}/{id}") // Acción de Borrar reserva
	public String borrarReservas(Model model,@PathVariable Long idUsuario, @PathVariable Long id) {
		
		Optional<Reserva> optional = repositorioReservas.findById(id);
		
		if (optional.isPresent()) {
			Reserva res = optional.get();
			repositorioReservas.deleteById(id);
			THabitacion tipo = res.getHabitacion().getTipohabitacion();
			Optional<Usuario> user = repositorioUsuarios.findById(idUsuario);
			if (user.isPresent()) {
				Usuario usuario =user.get();
				usuario.borrarPuntos(tipo);
				usuario.setNumReservas(usuario.getNumReservas() - 1);
				repositorioUsuarios.save(usuario);
			}
			return "redirect:/reservas/" + idUsuario;
		}
		else {
			model.addAttribute("message", "Error, reserva no encontrada.");
            return "error";
		}
	}
	@GetMapping("/editarreserva/{idUsuario}/{id}") // nos lleva al formulario de editar reserva
	public String editarReserva(Model model,@PathVariable Long idUsuario, @PathVariable Long id) {
			
		model.addAttribute("idReserva", id);
		model.addAttribute("idUsuario", idUsuario);
		
		return "formularioEditarReserva";
		
		
	}
	@PostMapping("/editarreserva/{idUsuario}/{idReserva}") //POST de editar reserva
	public String editarReservaPOST(Model model, @PathVariable Long idUsuario, @PathVariable Long idReserva, @RequestParam String tipoHabitacion, @RequestParam String fechaEntrada,
			@RequestParam String fechaSalida) {
			
		//Cogemos la reserva ya existente
		
		Optional<Reserva> optionalreserva = repositorioReservas.findById(idReserva);
		if (optionalreserva.isEmpty()) {
			
			model.addAttribute("message", "Error, reserva la reserva no existe.");
            return "errorFormularioEditarReserva";
		}
		Reserva reserva = optionalreserva.get();
		
		//Comprobamos que el formato de las fechas sea el correcto
		
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
			    LocalDate fechaInicioReserva2 = LocalDate.parse(fechaEntrada, formatter);

			    LocalDate fechaFinReserva2 = LocalDate.parse(fechaSalida, formatter);
				
				//Convertimos el tipohabitacion a THabitacion
				THabitacion tipo = null;
				switch(tipoHabitacion){
				case "Normal": tipo = THabitacion.NORMAL;
					break;
				case "Doble": tipo = THabitacion.DOBLE;
					break;
				case "Suite": tipo = THabitacion.SUITE;
					break;
				}
				
				// Buscamos el hotel, al modificar la reserva no se puede modificar el hotel
				Hotel hotel = reserva.getHotel();
	
				//Buscamos las habitaciones
				Optional<List<Habitacion>> optionalHabitaciones = repositorioHabitacion.findByHotel(hotel);
				if (optionalHabitaciones.isEmpty()) {
					model.addAttribute("message", "Error, habitaciones no encontradas.");
		            return "errorFormularioEditarReserva";
				}else {
					boolean haytipo = false;
					List<Habitacion> listaHabitaciones = optionalHabitaciones.get();
					ArrayList<Habitacion> habitacionestipo = new ArrayList<>();
					for (Habitacion hab: listaHabitaciones) {
						if (hab.getTipohabitacion().equals(tipo)) {
							habitacionestipo.add(hab);
							haytipo = true;
						}
					}
					//Si llegamos aqui hay que comprobar el boolean
					if (haytipo != true) { // no hay habitaciones
						model.addAttribute("message", "Error, no existen habitaciones en este hotel del tipo deseado.");
			            return "errorFormularioEditarReserva";
					}
					else {
						// Existen habitaciones y hay que comprobar las fechas
						boolean reservarokhotel = false;
						for (Habitacion habi: habitacionestipo) { //iteramos las posibles habitaciones
							List<Reserva> reservashab = habi.getReservas();
							boolean reservarokhab = true; //este boolean me marca si puedo reservar en una habitacion tras haber comprobado todas las reservas de la misma
							for (Reserva posiblereserva: reservashab) { //iteramos las reservas de la habitacion
								ReservaService servicioReservas = new ReservaService();
								if (servicioReservas.reservaComparator(posiblereserva, fechaInicioReserva2, fechaFinReserva2) == false) {
									//no se puede , ya que hay una reserva
									reservarokhab = false;
									
									break;	
								}	
							}
							if (reservarokhab == true) {
								// realizamos la reserva
								/* 1º - Creamos la Reserva
								 * 2º- La añadimos a la Habitacion
								 * 3º - Guardamos en el repositorio de habitaciones
								 * 4º - Guardamos en el repositorio de reservas
								 */
								reservarokhotel = true;
								//1º PASO
								reserva.setHabitacion(habi);
								reserva.setFechaInicio(fechaInicioReserva2);
								reserva.setFechaFin(fechaFinReserva2);
								
								// Añadimos a la reserva el hotel y el usuario
								
								reserva.setHotel(hotel);
								Usuario user = repositorioUsuarios.findById(idUsuario).get(); //Quizas habría que hacer un tratamiento de excepcion aqui en el caso de que no haya usuario
								reserva.setUsuario(user);
								user.cambiarPuntos(reserva.getHabitacion().getTipohabitacion(), tipo);
								//2º PASO
								habi.eliminarReserva(idReserva); //eliminamos la reserva antigua
								habi.anadirReserva(reserva); // añadimos la nueva
								//3º PASO
								repositorioHabitacion.save(habi);
								//4º PASO
								repositorioReservas.save(reserva);
								repositorioUsuarios.save(user);
								break;
							}
							
						} // se acaba el primer for 
						if (reservarokhotel == true) {
							return "redirect:/reservas/" + idUsuario;
						}else {
							model.addAttribute("message", "Error, no es posible reservar este tipo de habitación para este hotel en las fechas seleccionadas.");
				            return "errorFormularioEditarReserva";
						}	
					}
				}			
	}
	@GetMapping("/historialreservas/{id}") //muestra el historial de reservas realizadas y acabadas.
	public String historialreservas(Model model, @PathVariable Long id) {
		
		Optional<Usuario> optional = repositorioUsuarios.findById(id);
		
		if (optional.isEmpty()) {
			model.addAttribute("message", "Error, Usuario no encontrado.");
            return "error";
		}
		Usuario user= optional.get();
		model.addAttribute("usuario", user);
		model.addAttribute("numReservas", user.getNumReservas());
		model.addAttribute("id", id);
		return "historialReservas";
		
	}
	
	//CONTROLADOR MENU ADMIN
	@GetMapping("/menuAdmin") // Devuelve la vista de menu de admin
	public String menuAdmin(Model model) {
		return "menuAdmin";
	}
	//CONTROLADOR DE PROMOCIONES
	@GetMapping("/promociones") // devuelve la vista del menu promociones
	public String promociones(Model model) {
		List<Promocion> promociones = repositorioPromociones.findAll();
		model.addAttribute("promociones", promociones);
		
		return "gestionPromociones";
	}
	
	@GetMapping("/formularioPromocion") //Vista formulario promocion
	public String formularioPromocion(Model model) {
		
		return "formularioPromocion";
	}
	@PostMapping("/crearpromocion") //POST para crear promocion
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
	@GetMapping("/borrarpromocion/{id}") //Delete de borrar promocion
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
	
	@GetMapping("/hoteles") //Vista de los hoteles 
	public String hotelesyHabitaciones(Model model) {
		List<Hotel> hoteles = repositorioHotel.findAll();
		model.addAttribute("hoteles", hoteles);
		
		return "gestionHoteles";
	}
	@PostMapping("/borrarhotel") //Borrar hoteles
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
	
	@PostMapping("/crearhotel") //POST Crear hoteles
	public String crearhotel(Model model, @RequestParam String nombrehotel) {
		Hotel nuevoHotel = new Hotel (nombrehotel);
		repositorioHotel.save(nuevoHotel);
		
		return "redirect:/hoteles";
	}
	//CONTROLADOR GESTION DE HABITACIONES
	@GetMapping("/hotelesyhabitaciones/{id}") //Vista que muestra las habitaciones de un hotel
	public String hotelesyHabitaciones(Model model, @PathVariable Long id) {
		
		Optional<Hotel> optional = repositorioHotel.findById(id);
		
		if (optional.isPresent()) {
			Hotel hotel = optional.get();
			model.addAttribute("id", hotel.getHotelId());
			model.addAttribute("nombre",hotel.getNombre());
			model.addAttribute("numHabitaciones",hotel.getNumHabitaciones());
			model.addAttribute("habitaciones", hotel.getHabitaciones());
			model.addAttribute("imagenes", hotel.getImagenes());
			
		
			return "gestionHabitaciones";
		}
		else {
			model.addAttribute("message", "Se ha producido un error, hotel no encontrado.");
			return "error";
		}
		
	}
	@GetMapping("/anadirhabitacion/{id}") //Formulario de añadir habitacion
	public String anadirhabitacion(Model model, @PathVariable Long id) {
		model.addAttribute("identificadorHotel", id);
		return "formularioNuevaHabitacion" ;
	}
	
	@PostMapping("/anadirhabitacion/{id}") //Post para añadir habitacion
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
	@GetMapping("/borrarhabitacion/{id}/{habitacionId}") //Borrar habitacion
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
	@GetMapping("/errorInicioSesion")
	public String errorInicioSesionUsuario(Model model) {
		model.addAttribute("message", "Error en el incio de sesión. Las credenciales no coinciden.");
		return "error";
	}
}
