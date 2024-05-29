package com.example.demo.classes;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;





@Entity
public class Reserva {
		
	   	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    private LocalDate fechaInicio;
	    private LocalDate fechaFin;
	    
	    @ManyToOne
	    private Hotel hotel;
	    
	    @ManyToOne
	    private Habitacion habitacion;
	    
	    @ManyToOne
	    private Usuario usuario;
	    
	    
		public Reserva () {}
		
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

		public void setId(Long id) {
			this.id = id;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public Hotel getHotel() {
			return hotel;
		}

		public void setHotel(Hotel hotel) {
			this.hotel = hotel;
		}					
	    
	    
	    
}
