package ar.edu.uade.grupo5.divisiongastos.web;

import java.math.BigDecimal;

public record TransferView(String fromName, String toName, BigDecimal amount) {
}
