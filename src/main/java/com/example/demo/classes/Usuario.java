package com.example.demo.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {
	
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private String contraseña;
		private String username;
		private LocalDate fechaAlta;
		private Integer puntos;
		private Integer numReservas;
		
		@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Reserva> reservas;
		
		@OneToMany
		private List<Reserva> historialReservas;
		
		protected Usuario() {}
		public Usuario(String nombre, String password,LocalDate fechaAlta) {
			this.username= nombre;
			this.contraseña = password;
			this.fechaAlta = fechaAlta;
			this.numReservas = 0;
			this.puntos = 0;
			
		}
		public String getContraseña() {
			return contraseña;
		}
		public void setContraseña(String contraseña) {
			this.contraseña = contraseña;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public LocalDate getFechaAlta() {
			return fechaAlta;
		}
		public void setFechaAlta(LocalDate fechaAlta) {
			this.fechaAlta = fechaAlta;
		}
		public Integer getPuntos() {
			return puntos;
		}
		public void setPuntos(Integer puntos) {
			this.puntos = puntos;
		}
		public Integer getNumReservas() {
			return numReservas;
		}
		public void setNumReservas(Integer numReservas) {
			this.numReservas = numReservas;
		}
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		public List<Reserva> getReservas() {
			return reservas;
		}
		public void setReservas(List<Reserva> reservas) {
			this.reservas = reservas;
		}
		public List<Reserva> getHistorialReservas() {
			return historialReservas;
		}
		public void setHistorialReservas(List<Reserva> historialReservas) {
			this.historialReservas = historialReservas;
		}
		
}
