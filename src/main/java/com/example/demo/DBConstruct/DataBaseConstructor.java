package com.example.demo.DBConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.demo.classes.Habitacion;
import com.example.demo.classes.HabitacionRepository;
import com.example.demo.classes.Hotel;
import com.example.demo.classes.HotelRepository;
import com.example.demo.classes.Reserva;
import com.example.demo.classes.ReservaRepository;
import com.example.demo.classes.THabitacion;
import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;

import jakarta.annotation.PostConstruct;

@Component
@Profile("local")
 public class DataBaseConstructor {

	@Autowired
	private HotelRepository repositorioHotel;
	
	@Autowired
	private HabitacionRepository repositorioHabitacion;
	
	@Autowired
	private ReservaRepository repositorioReservas;
	
	@Autowired
	private UsuarioRepository repositorioUsuarios;
	
	@PostConstruct
	public void inicializarBBDD() {
		//Creamos Hoteles
		Hotel h1 = new Hotel("Coliseo");
		Hotel h2 = new Hotel("Gran España");
		Hotel h3 = new Hotel("Blue Lagoon");
		
		//Creamos Habitaciones
		Habitacion hab1 = new Habitacion (h1,"A101", THabitacion.SUITE);
		Habitacion hab2 = new Habitacion (h1, "A102", THabitacion.NORMAL);
		Habitacion hab3 = new Habitacion (h1, "A103", THabitacion.DOBLE);
		
		Habitacion hab4 = new Habitacion (h2, "B101", THabitacion.SUITE);
		Habitacion hab5 = new Habitacion (h2, "B102", THabitacion.NORMAL);
		Habitacion hab6 = new Habitacion (h2, "B103", THabitacion.DOBLE);
		
		Habitacion hab7 = new Habitacion (h3, "C101", THabitacion.SUITE);
		Habitacion hab8 = new Habitacion (h3, "C102", THabitacion.NORMAL);
		Habitacion hab9 = new Habitacion (h3, "C103", THabitacion.DOBLE);
		
		
		
		//Creamos Reservas
		
		LocalDate inicio1 = LocalDate.of(2024, 6, 15);
		LocalDate inicio2 = LocalDate.of(2024, 6, 11);
		LocalDate inicio3 = LocalDate.of(2024, 7, 3);
		
		LocalDate final1 = LocalDate.of(2024, 6, 19);
		LocalDate final2 = LocalDate.of(2022, 6, 21);
		LocalDate final3 = LocalDate.of(2022, 7, 15);

		Reserva res1 = new Reserva(inicio1, final1, hab1);
		Reserva res2 = new Reserva(inicio2, final2, hab4);
		Reserva res3 = new Reserva(inicio3, final3, hab8);
	
		
		//enlazamos habitacion con hotel & reservas con habitaciones
		
		hab1.setHotel(h1);
		hab2.setHotel(h1);
		hab3.setHotel(h1);
		
		ArrayList<Habitacion> habitaciones1 = new ArrayList<>();
		ArrayList<Habitacion> habitaciones2 = new ArrayList<>();
		ArrayList<Habitacion> habitaciones3 = new ArrayList<>();
		
		habitaciones1.addAll(Arrays.asList(hab1,hab2,hab3));
		
		h1.setHabitaciones(habitaciones1);
		
		hab4.setHotel(h2);
		hab5.setHotel(h2);
		hab6.setHotel(h2);
		
		habitaciones2.addAll(Arrays.asList(hab4,hab5,hab6));
		h2.setHabitaciones(habitaciones2);
		
		hab7.setHotel(h3);
		hab8.setHotel(h3);
		hab9.setHotel(h3);
		
		habitaciones3.addAll(Arrays.asList(hab7,hab8,hab9));
		h3.setHabitaciones(habitaciones3);
	
		hab1.anadirReserva(res1);
		hab4.anadirReserva(res2);
		hab8.anadirReserva(res3);
		
		
		//creamos usuarios
		
		Usuario us1 = new Usuario("Alpaca1200","Amigo1",LocalDate.of(2020, 1, 1));
		Usuario us2 = new Usuario("Carla Gonzalez","Carla1200",LocalDate.of(2020, 2, 2));
		Usuario us3 = new Usuario("Stephan Uric","Croatia1999",LocalDate.of(2020, 3, 3));
		
		//introducimos en repositorios
		
		repositorioHotel.save(h1);
		repositorioHotel.save(h2);
		repositorioHotel.save(h3);
		repositorioHabitacion.saveAll(Arrays.asList(hab1,hab2,hab3,hab4,hab5,hab6,hab7,hab8,hab9));
		repositorioReservas.saveAll(Arrays.asList(res1,res2,res3));
		repositorioUsuarios.saveAll(Arrays.asList(us1,us2,us3));
		
		
	}
}
