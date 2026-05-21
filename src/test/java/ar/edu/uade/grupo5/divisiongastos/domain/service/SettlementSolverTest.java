package ar.edu.uade.grupo5.divisiongastos.domain.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementSolverTest {

    private final SettlementSolver solver = new SettlementSolver();
    private final MemberId anaId = new MemberId("ana");
    private final MemberId betoId = new MemberId("beto");
    private final MemberId caroId = new MemberId("caro");

    @Test
    void returnsNoTransfersWhenAlreadySettled() {
        var transfers = solver.solve(Map.of(
                anaId, new BigDecimal("0.00"),
                betoId, new BigDecimal("0.00")
        ));

        assertThat(transfers).isEmpty();
    }

    @Test
    void createsExpectedTransferForSimpleDebt() {
        var transfers = solver.solve(Map.of(
                anaId, new BigDecimal("50.00"),
                betoId, new BigDecimal("-50.00")
        ));

        assertThat(transfers).singleElement().satisfies(transfer -> {
            assertThat(transfer.from()).isEqualTo(betoId);
            assertThat(transfer.to()).isEqualTo(anaId);
            assertThat(transfer.amount()).isEqualByComparingTo("50.00");
        });
    }

    @Test
    void settlesMultipleMembersWithMinimalTransfers() {
        var transfers = solver.solve(Map.of(
                anaId, new BigDecimal("70.00"),
                betoId, new BigDecimal("-40.00"),
                caroId, new BigDecimal("-30.00")
        ));

        assertThat(transfers).hasSize(2);
        assertThat(transfers)
                .extracting(transfer -> transfer.amount())
                .containsExactly(new BigDecimal("40.00"), new BigDecimal("30.00"));
    }
}
