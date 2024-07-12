package com.pichincha.Cuentas.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pichincha.Cuentas.model.Movimientos;
import com.pichincha.Cuentas.repository.MovimientoRepository;

@Service
public class MovimientoService {

	@Autowired
	private MovimientoRepository movimientoRepository;

	public List<Movimientos> findAll() {
		return movimientoRepository.findAll();
	}

	public Optional<Movimientos> findById(long id) {
		return movimientoRepository.findById(id);
	}

	public Movimientos save(Movimientos movimientos) {
		return movimientoRepository.save(movimientos);
	}

	public void deleteById(long id) {
		movimientoRepository.deleteById(id);
	}

	public List<Movimientos> findByIdAndFechaBetween(long id, Date startDate, Date endDate) {
		return movimientoRepository.findByIdAndFechaBetween(id, startDate, endDate);
	}

}
