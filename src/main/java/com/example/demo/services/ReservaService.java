package com.example.demo.services;

import java.time.LocalDate;


import org.springframework.stereotype.Service;

import com.example.demo.classes.Reserva;


@Service
public class ReservaService {
	
	public ReservaService() {}
	
	//Esta funcion compara dos fechas con una reserva dada y devuelve true si no hay colisiones y false si las hay
	public boolean reservaComparator (Reserva reserva1, LocalDate fechaInicio , LocalDate fechaFin){
		
		boolean devolver;
							
		    LocalDate fechaInicioReserva1 = reserva1.getFechaInicio();
			
			LocalDate fechaFinReserva1 =  reserva1.getFechaFin();
			
			
			if ((fechaInicio.isBefore(fechaInicioReserva1) && fechaFin.isBefore(fechaInicioReserva1)) || (fechaInicio.isAfter(fechaFinReserva1) && fechaFin.isAfter(fechaFinReserva1))) {
				devolver = true; //devuelve true cuando hay disponibilidad
			}else devolver = false;
			
			return devolver;
		
		}
	//verifica que la fecha de entrada sea anterior a la fecha de salida
	public boolean fechasCorrectasReserva(LocalDate fechaEntrada, LocalDate fechaSalida) {
		
		if (fechaEntrada.isBefore(fechaSalida)) return true;
		else return false;
	}
}
	

