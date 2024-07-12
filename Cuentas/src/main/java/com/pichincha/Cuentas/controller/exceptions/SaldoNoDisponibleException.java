package com.pichincha.Cuentas.controller.exceptions;

public class SaldoNoDisponibleException extends RuntimeException {

	private static final long serialVersionUID = 6703995596852287652L;
	private static final String DESCRIPTION = "Saldo no disponible";

	public SaldoNoDisponibleException(String detail) {
		super(DESCRIPTION + ". " + detail);
	}

}
