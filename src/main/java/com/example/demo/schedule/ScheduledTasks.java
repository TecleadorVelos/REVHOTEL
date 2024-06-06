package com.example.demo.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.classes.Reserva;
import com.example.demo.classes.Usuario;
import com.example.demo.classes.UsuarioRepository;

@Component
public class ScheduledTasks {

	private UsuarioRepository repositorioUsuarios;
	
	@Scheduled(cron = "0 0 0 * * *") // Ejecuta a la medianoche todos los días
	public void reservasActivasaHistorial() {
			List<Usuario> listaUsuarios = repositorioUsuarios.findAll();
			LocalDate fechaActual = LocalDate.now();
			
			for (Usuario u: listaUsuarios) { //Para cada usuario
				List<Reserva> reservasActivas = u.getReservas();
				List<Reserva> historialReservas = u.getHistorialReservas();
				List<Reserva> listaBorrar = new ArrayList<>();
				for (Reserva res : reservasActivas) {//Cogemos las reservas y vemos si la fecha actual es posterior a la fecha de fin de la reserva
					LocalDate fechaFinres = res.getFechaFin();
					if (fechaFinres.isAfter(fechaActual)) {
						historialReservas.add(res);
						listaBorrar.add(res);  //Añadimos en una lista todas las reservas que vamos a borrar. No podemos borrar en mitad de la iteracion.	
					}
				}
				Integer numReservasElim = listaBorrar.size();
				for(Reserva reserv : listaBorrar) {
					reservasActivas.remove(reserv);
				}
				//Actualizamos usuario y lo guardamos en repositorio
				u.setNumReservas(u.getNumReservas() - numReservasElim);
				u.setReservas(reservasActivas);
				u.setHistorialReservas(historialReservas);
				
				repositorioUsuarios.save(u);
			}
			 
	}
	
	
	
}
