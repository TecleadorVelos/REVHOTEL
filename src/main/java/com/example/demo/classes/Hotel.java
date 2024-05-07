package com.example.demo.classes;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;




@Entity
public class Hotel {
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long hotelId;
		private String nombre;
		
		@OneToMany(cascade=CascadeType.ALL, mappedBy="hotel")
		private List<Habitacion> habitaciones = new ArrayList<Habitacion>(); //Podria mejorarse con un mapa, considerar en un futuro
		
		protected Hotel() {}
		
		public Hotel (String name) {
			this.nombre = name;
			this.habitaciones = null;
		}


		public String getNombre() {
			return nombre;
		}


		public void setNombre(String nombre) {
			this.nombre = nombre;
		}


		public List<Habitacion> getHabitaciones() {
			return habitaciones;
		}


		public void setHabitaciones(ArrayList<Habitacion> habitaciones) {
			this.habitaciones = habitaciones;
		}
		
		public void anadirHabitacion(Habitacion nuevaHabitacion) {
			this.habitaciones.add(nuevaHabitacion);
		}
		
		public boolean eliminarHabitacion (String numero) {
			boolean operacion = false;
			Habitacion hab = null;
			for (Habitacion habitaciontemp: this.habitaciones) {
				if (habitaciontemp.getNumero()== numero) {
					hab = habitaciontemp;
					operacion = true;
				}
			}
			this.habitaciones.remove(hab);
			if (operacion == true) return true;
			else return false;
		}
}
