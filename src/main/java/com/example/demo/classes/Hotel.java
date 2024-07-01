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
		private Integer numHabitaciones;
		private List<String> imagenes = new ArrayList<>();
		

		protected Hotel() {}
		
		public Hotel (String name) {
			this.nombre = name;
			this.habitaciones = null;
			this.numHabitaciones = 0;
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
		
		public Long getHotelId() {
			return hotelId;
		}

		public Integer getNumHabitaciones() {
			return numHabitaciones;
		}
		
		public void setNumHabitaciones(Integer numHabitaciones) {
			this.numHabitaciones = numHabitaciones;
		}

		public void anadirHabitacion(Habitacion nuevaHabitacion) {
			this.habitaciones.add(nuevaHabitacion);
			this.numHabitaciones++;
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
			if (operacion == true) {
				this.numHabitaciones--;
				return true;
			}
			else return false;
		}
		public List<String> getImagenes() {
			return imagenes;
		}

		public void setImagenes(List<String> imagenes) {
			this.imagenes = imagenes;
		}
}
