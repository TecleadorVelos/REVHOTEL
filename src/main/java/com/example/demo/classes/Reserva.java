package com.example.demo.classes;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;





@Entity
public class Reserva {
		
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private LocalDate fechaInicio;
	    private LocalDate fechaFin;
	    
	    @ManyToOne
	    private Habitacion habitacion;
	    
	    //Probablemente haya que añadir el usuario que ha relaizado la reserva
		public Reserva () {
			this.fechaFin = null;
	    	this.fechaInicio = null;
	    	this.habitacion = null;
		}
	    public Reserva (LocalDate inicio, LocalDate fin, Habitacion hab) {
	    	this.fechaFin = fin;
	    	this.fechaInicio = inicio;
	    	this.habitacion = hab;
	    }
	    
	    public LocalDate getFechaInicio() {
			return fechaInicio;
		}

		public void setFechaInicio(LocalDate fechaInicio) {
			this.fechaInicio = fechaInicio;
		}

		public LocalDate getFechaFin() {
			return fechaFin;
		}

		public void setFechaFin(LocalDate fechaFin) {
			this.fechaFin = fechaFin;
		}

		public Habitacion getHabitacion() {
			return habitacion;
		}

		public void setHabitacion(Habitacion habitacion) {
			this.habitacion = habitacion;
		}

		public Long getId() {
			return id;
		}
	    
	    
	    
}
