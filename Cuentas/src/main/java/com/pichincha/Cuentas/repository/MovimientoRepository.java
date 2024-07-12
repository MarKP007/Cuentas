package com.pichincha.Cuentas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pichincha.Cuentas.model.Movimientos;

public interface MovimientoRepository extends JpaRepository<Movimientos, Long> {

	// @Query(value = "SELECT m.id, m.fecha, m.tipomovimiento, m.valor, m.saldo,
	// m.cuentaid FROM movimientos m WHERE m.cuentaid = :id AND m.fecha BETWEEN
	// :startDate AND :endDate", nativeQuery = true)
	@Query(value = "SELECT m.id, m.fecha, m.tipomovimiento, m.valor, m.saldo, m.cuentaid FROM movimientos m WHERE m.cuentaid = ?1 AND m.fecha BETWEEN ?2 AND ?3", nativeQuery = true)
	List<Movimientos> findByIdAndFechaBetween(@Param("id") long id, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

}
