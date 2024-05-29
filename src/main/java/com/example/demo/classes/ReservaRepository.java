package com.example.demo.classes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ReservaRepository extends JpaRepository<Reserva, Long>{
		
		Optional<List<Reserva>> findByUsuario(Usuario user);
}
