package ar.edu.uade.grupo5.divisiongastos.domain.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Transfer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SettlementSolver {

    public List<Transfer> solve(Map<MemberId, BigDecimal> balance) {
        var debtors = new ArrayList<Account>();
        var creditors = new ArrayList<Account>();

        balance.forEach((memberId, amount) -> {
            BigDecimal normalized = money(amount);
            if (normalized.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new Account(memberId, normalized.abs()));
            } else if (normalized.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new Account(memberId, normalized));
            }
        });

        debtors.sort(Comparator.comparing(Account::amount).reversed());
        creditors.sort(Comparator.comparing(Account::amount).reversed());

        var transfers = new ArrayList<Transfer>();
        int debtorIndex = 0;
        int creditorIndex = 0;
        while (debtorIndex < debtors.size() && creditorIndex < creditors.size()) {
            Account debtor = debtors.get(debtorIndex);
            Account creditor = creditors.get(creditorIndex);
            BigDecimal amount = debtor.amount().min(creditor.amount());
            transfers.add(new Transfer(debtor.memberId(), creditor.memberId(), amount));

            debtors.set(debtorIndex, debtor.subtract(amount));
            creditors.set(creditorIndex, creditor.subtract(amount));

            if (debtors.get(debtorIndex).isSettled()) {
                debtorIndex++;
            }
            if (creditors.get(creditorIndex).isSettled()) {
                creditorIndex++;
            }
        }

        return List.copyOf(transfers);
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private record Account(MemberId memberId, BigDecimal amount) {

        private Account subtract(BigDecimal paid) {
            return new Account(memberId, amount.subtract(paid).setScale(2, RoundingMode.HALF_UP));
        }

        private boolean isSettled() {
            return amount.compareTo(BigDecimal.ZERO) == 0;
        }
    }
}
