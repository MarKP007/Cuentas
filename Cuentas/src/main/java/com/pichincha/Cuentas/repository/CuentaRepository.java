package com.pichincha.Cuentas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pichincha.Cuentas.model.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	List<Cuenta> findByClientId(long clienteId);

	void deleteByClientId(long clientId);

}
