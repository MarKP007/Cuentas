package com.pichincha.Cuentas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pichincha.Cuentas.controller.exceptions.SaldoNoDisponibleException;
import com.pichincha.Cuentas.model.Cuenta;
import com.pichincha.Cuentas.model.Movimientos;
import com.pichincha.Cuentas.service.CuentaService;
import com.pichincha.Cuentas.service.MovimientoService;
import com.pichincha.Cuentas.utils.PichinchaUtil;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

	@Autowired
	CuentaService cuentaService;

	@Autowired
	MovimientoService movimientoService;

	@GetMapping
	public ResponseEntity<List<Movimientos>> obtenerTodosMovimientos() {
		try {
			List<Movimientos> movimientos = new ArrayList<Movimientos>();

			movimientoService.findAll().forEach(movimientos::add);

			if (movimientos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(movimientos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Movimientos> obtenerMovimientoPorId(@PathVariable("id") long id) {
		Optional<Movimientos> movimientoData = movimientoService.findById(id);

		if (movimientoData.isPresent()) {
			return new ResponseEntity<>(movimientoData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/{cuentaId}")
	public ResponseEntity<Movimientos> crearMovimiento(@PathVariable Long cuentaId,
			@RequestBody Movimientos movimiento) {
		Optional<Cuenta> cuentaeData = cuentaService.findById(cuentaId);
		Cuenta cuenta;
		if (cuentaeData.isPresent()) {
			cuenta = cuentaeData.get();
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		cuenta.setSaldoInicial(cuenta.getSaldoInicial() + movimiento.getValor());
		if (cuenta.getSaldoInicial() < 0) {
			throw new SaldoNoDisponibleException("");
		}
		cuentaService.save(cuenta);

		String tipoMovimiento = movimiento.getValor() < 0 ? "RETIRO" : "DEPOSITO";
		try {
			Movimientos _movimiento = movimientoService.save(new Movimientos(new Date(), tipoMovimiento,
					movimiento.getValor(), cuenta.getSaldoInicial(), cuenta.getId()));
			_movimiento.setCuentaId(cuenta.getId());
			return new ResponseEntity<>(_movimiento, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Movimientos> updateMovimiento(@PathVariable("id") long id,
			@RequestBody Movimientos movimiento) {
		Optional<Movimientos> movimientoData = movimientoService.findById(id);

		if (movimientoData.isPresent()) {
			Movimientos _movimiento = movimientoData.get();
			_movimiento.setFecha(
					PichinchaUtil.esCampoLleno(movimiento.getFecha()) ? movimiento.getFecha() : _movimiento.getFecha());
			_movimiento.setTipoMovimiento(
					PichinchaUtil.esCampoLleno(movimiento.getTipoMovimiento()) ? movimiento.getTipoMovimiento()
							: _movimiento.getTipoMovimiento());
			_movimiento.setValor(
					PichinchaUtil.esCampoLleno(movimiento.getValor()) ? movimiento.getValor() : _movimiento.getValor());
			_movimiento.setSaldo(
					PichinchaUtil.esCampoLleno(movimiento.getSaldo()) ? movimiento.getSaldo() : _movimiento.getSaldo());
			return new ResponseEntity<>(movimientoService.save(_movimiento), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> eliminarMovimiento(@PathVariable("id") long id) {
		try {
			movimientoService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/movimientos")
	public List<Movimientos> getMovimientos(@RequestParam long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
		return movimientoService.findByIdAndFechaBetween(id, startDate, endDate);
	}

}
