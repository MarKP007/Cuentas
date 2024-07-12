package com.pichincha.Cuentas.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pichincha.Cuentas.configuration.FeignCuentaConfig;
import com.pichincha.Cuentas.model.ClienteDTO;

@FeignClient(name = "Cliente", url = "${cliente_url}", configuration = FeignCuentaConfig.class)
public interface ToClienteFromCuenta {

	@GetMapping("/clientes/{id}")
	ResponseEntity<ClienteDTO> getClientById(@PathVariable("id") long id);
}
