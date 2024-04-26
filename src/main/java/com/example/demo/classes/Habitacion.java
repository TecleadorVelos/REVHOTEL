package com.example.demo.classes;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity
public class Habitacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long habitacionId;
	private THabitacion tipohabitacion;
	private Integer precio; /* Precio por noche */
	private String numero;
	
	@OneToMany
	private List<Reserva> reservas;
	
	public Habitacion(THabitacion thabi, String number) {
		
		this.tipohabitacion = thabi;
		this.numero = number;
		
		switch (thabi) {
		case SUITE: 
			this.precio = 100;
			break;
		case DOBLE:
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
		reservas = null;
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Long getHabitacionId() {
		return habitacionId;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	
	public void anadirReserva (Reserva res) {
		this.reservas.add(res);
	}
	
	public boolean eliminarReserva (Reserva res) {
		boolean operacion = false;
		operacion = this.reservas.remove(res);
		if (operacion == true) return true;
		else return false;
	}
	
	
}
