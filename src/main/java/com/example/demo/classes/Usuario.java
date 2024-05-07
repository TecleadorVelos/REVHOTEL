package com.example.demo.classes;

import java.time.LocalDate;
import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {
	
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private String contraseña;
		private String nombreCompleto;
		private LocalDate fechaAlta;
		private Integer puntos;
		private Integer numReservas;
		
		
		protected Usuario() {}
		public Usuario(String nombre, String password,LocalDate fechaAlta) {
			this.nombreCompleto= nombre;
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
		public String getNombreCompleto() {
			return nombreCompleto;
		}
		public void setNombreCompleto(String nombreCompleto) {
			this.nombreCompleto = nombreCompleto;
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
		
		
		
}
