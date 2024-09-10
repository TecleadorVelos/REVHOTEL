package com.example.demo.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class PromocionService {

	public PromocionService() {}
	
		//verifica que la fecha de entrada sea anterior a la fecha de salida
		public boolean fechasCorrectasPromocion(LocalDate fechaInicio, LocalDate fechaFin) {
			
			if (fechaInicio.isBefore(fechaFin)) return true;
			else return false;
		}
}
