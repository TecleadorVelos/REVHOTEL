package com.example.demo.classes;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;




@Entity
public class Habitacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long habitacionId;
	private THabitacion tipohabitacion;
	private Integer precio; /* Precio por noche */
	private String numero;
	
	@OneToMany(mappedBy="habitacion")
	private List<Reserva> reservas = new ArrayList<>();
	
	@ManyToOne
	private Hotel hotel;
	
	protected Habitacion() {}
	public Habitacion(Hotel hotel, String number, THabitacion thabi) {
		
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
		
		this.hotel = hotel;
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

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	
	public void anadirReserva (Reserva res) {
		this.reservas.add(res);
	}
	
	public boolean eliminarReserva (Long id) {
		boolean operacion = false;
		Reserva res = null;
		for (Reserva reservatemp: this.reservas) {
			if (reservatemp.getId()== id) {
				res = reservatemp;
				operacion = true;
			}
		}
		this.reservas.remove(res);
		if (operacion == true) return true;
		else return false;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	
}
