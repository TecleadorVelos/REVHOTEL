package com.example.classes;

public class Habitacion {
	
	private THabitacion tipohabitacion;
	private Integer precio; /* Precio por noche */
	private String numero;
	
	public Habitacion(THabitacion thabi, String numero) {
		
		this.tipohabitacion = thabi;
		this.numero = numero;
		
		switch (thabi) {
		case SUITE: 
			this.precio = 100;
			break;
		case DELUXE:
			this.precio = 50;
			break;
		case NORMAL:
			this.precio = 25;
			break;
		default:
			this.tipohabitacion = THabitacion.NORMAL;
			this.precio = 25;
			break;
		}
	}

	public THabitacion getTipohabitacion() {
		return tipohabitacion;
	}

	public void setTipohabitacion(THabitacion tipohabitacion) {
		this.tipohabitacion = tipohabitacion;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}
	
}
