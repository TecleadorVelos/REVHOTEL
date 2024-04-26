package com.example.demo.classes;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity
public class Hotel {
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long hotelId;
		private String nombre;
		
		@OneToMany
		private ArrayList<Habitacion> habitaciones; //Podria mejorarse con un mapa, considerar en un futuro
		

		public Hotel (String name) {
			this.nombre = name;
			habitaciones = new ArrayList<>();
		}


		public String getNombre() {
			return nombre;
		}


		public void setNombre(String nombre) {
			this.nombre = nombre;
		}


		public ArrayList<Habitacion> getHabitaciones() {
			return habitaciones;
		}


		public void setHabitaciones(ArrayList<Habitacion> habitaciones) {
			this.habitaciones = habitaciones;
		}
		
}
