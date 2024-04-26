package com.example.demo.classes;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
public class Reserva {
		
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private LocalDate fechaInicio;
	    private LocalDate fechaFin;
	    
	    @ManyToOne
	    private Habitacion habitacion;

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