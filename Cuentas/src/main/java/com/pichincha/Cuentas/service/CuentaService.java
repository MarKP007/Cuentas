package com.pichincha.Cuentas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pichincha.Cuentas.model.Cuenta;
import com.pichincha.Cuentas.repository.CuentaRepository;

@Service
public class CuentaService {

	@Autowired
	private CuentaRepository cuentaRepository;

	public List<Cuenta> findAll() {
		return cuentaRepository.findAll();
	}

	public Optional<Cuenta> findById(long id) {
		return cuentaRepository.findById(id);
	}

	public Cuenta save(Cuenta cliente) {
		return cuentaRepository.save(cliente);
	}

	public void deleteById(long id) {
		cuentaRepository.deleteById(id);
	}

	public List<Cuenta> findByClientId(long clienteId) {
		return cuentaRepository.findByClientId(clienteId);
	}

	public void deleteByClientId(long clientId) {
		cuentaRepository.deleteAll(cuentaRepository.findByClientId(clientId));
	}

}
