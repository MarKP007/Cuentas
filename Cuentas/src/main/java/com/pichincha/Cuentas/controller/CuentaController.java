package com.pichincha.Cuentas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pichincha.Cuentas.model.ClienteDTO;
import com.pichincha.Cuentas.model.Cuenta;
import com.pichincha.Cuentas.repository.ToClienteFromCuenta;
import com.pichincha.Cuentas.service.CuentaService;
import com.pichincha.Cuentas.utils.PichinchaUtil;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

	@Autowired
	CuentaService cuentaService;

	@Autowired
	private ToClienteFromCuenta toClienteFromCuenta;

	@GetMapping
	public ResponseEntity<List<Cuenta>> obtenerTodosCuentas() {
		try {
			List<Cuenta> cuentas = new ArrayList<Cuenta>();

			cuentaService.findAll().forEach(cuentas::add);

			if (cuentas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(cuentas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cuenta> obtenerCuentaPorId(@PathVariable("id") long id) {
		Optional<Cuenta> cuentaData = cuentaService.findById(id);

		if (cuentaData.isPresent()) {
			return new ResponseEntity<>(cuentaData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/cliente/{clienteId}")
	public List<Cuenta> getCuentasByClienteId(@PathVariable long clienteId) {
		return cuentaService.findByClientId(clienteId);
	}

	@PostMapping("/{clienteId}")
	public ResponseEntity<Cuenta> crearCuenta(@PathVariable Long clienteId, @RequestBody Cuenta cuenta) {
		ResponseEntity<ClienteDTO> clienteDTO;
		try {
			clienteDTO = toClienteFromCuenta.getClientById(clienteId);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		try {
			Cuenta _cuenta = null;
			if (HttpStatus.OK.equals(clienteDTO.getStatusCode())) {
				_cuenta = cuentaService.save(new Cuenta(cuenta.getNumeroCuenta(), cuenta.getTipoCuenta(),
						cuenta.getSaldoInicial(), cuenta.getEstado(), clienteId));
			}
			return new ResponseEntity<>(_cuenta, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cuenta> updateCuenta(@PathVariable("id") long id, @RequestBody Cuenta cuenta) {
		Optional<Cuenta> cuentaData = cuentaService.findById(id);

		if (cuentaData.isPresent()) {
			Cuenta _cuenta = cuentaData.get();
			_cuenta.setNumeroCuenta(PichinchaUtil.esCampoLleno(cuenta.getNumeroCuenta()) ? cuenta.getNumeroCuenta()
					: _cuenta.getNumeroCuenta());
			_cuenta.setTipoCuenta(PichinchaUtil.esCampoLleno(cuenta.getTipoCuenta()) ? cuenta.getTipoCuenta()
					: _cuenta.getTipoCuenta());
			_cuenta.setSaldoInicial(PichinchaUtil.esCampoLleno(cuenta.getSaldoInicial()) ? cuenta.getSaldoInicial()
					: _cuenta.getSaldoInicial());
			_cuenta.setEstado(
					PichinchaUtil.esCampoLleno(cuenta.getEstado()) ? cuenta.getEstado() : _cuenta.getEstado());
			return new ResponseEntity<>(cuentaService.save(_cuenta), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> eliminarCuenta(@PathVariable("id") long id) {
		try {
			cuentaService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/cliente/{clientId}")
	public ResponseEntity<HttpStatus> deleteByClientId(@PathVariable long clientId) {
		try {
			cuentaService.deleteByClientId(clientId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
