package ar.edu.uade.grupo5.divisiongastos.quality;

import java.math.BigDecimal;

public class DemoQualityProbe {

    public String describe(BigDecimal amount, String userName) {
        if (amount == null) {
            System.out.println("Missing amount for " + userName);
            return "sin monto";
        }
        if (amount.signum() < 0) {
            return "monto negativo";
        }
        return "monto valido: " + amount;
    }

    private void unusedDemoHook() {
        System.out.println("Demo hook");
    }
}
