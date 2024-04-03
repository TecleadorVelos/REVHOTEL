package com.example.classes;

import java.util.ArrayList;

public class Hotel {

		private String nombre;
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
