package com.example.demo.classes;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Promocion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Double oferta;
	private LocalDate fechainicio;
	private LocalDate fechafinal;
	
	protected Promocion () {};
	
	public Promocion(Double porcentaje, LocalDate fechaini, LocalDate fechafinal) {
		this.oferta = porcentaje;
		this.fechainicio = fechaini;
		this.fechafinal = fechafinal;
	}

	public Double getOferta() {
		return oferta;
	}

	public void setOferta(Double oferta) {
		this.oferta = oferta;
	}

	public LocalDate getFechainicio() {
		return fechainicio;
	}

	public void setFechainicio(LocalDate fechainicio) {
		this.fechainicio = fechainicio;
	}

	public LocalDate getFechafinal() {
		return fechafinal;
	}

	public void setFechafinal(LocalDate fechafinal) {
		this.fechafinal = fechafinal;
	}

	public Long getId() {
		return id;
	}
	
	
}
