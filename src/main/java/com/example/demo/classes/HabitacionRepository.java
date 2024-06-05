package com.example.demo.classes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
	
	Optional<List<Habitacion>> findByHotel(Hotel hotel);
}
