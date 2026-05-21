package ar.edu.uade.grupo5.divisiongastos.domain.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BalanceCalculator {

    public Map<MemberId, BigDecimal> calculate(Group group, List<Expense> expenses) {
        var balance = new LinkedHashMap<MemberId, BigDecimal>();
        group.members().forEach(member -> balance.put(member.id(), money(BigDecimal.ZERO)));

        for (Expense expense : expenses) {
            balance.compute(expense.payerId(), (ignored, current) -> value(current).add(expense.amount()));

            var participants = new ArrayList<>(expense.participants());
            var shares = splitAmount(expense.amount(), participants.size());
            for (int index = 0; index < participants.size(); index++) {
                MemberId participant = participants.get(index);
                BigDecimal share = shares.get(index);
                balance.compute(participant, (ignored, current) -> value(current).subtract(share));
            }
        }

        balance.replaceAll((ignored, amount) -> money(amount));
        return Map.copyOf(balance);
    }

    private List<BigDecimal> splitAmount(BigDecimal amount, int participantCount) {
        BigDecimal baseShare = amount.divide(BigDecimal.valueOf(participantCount), 2, RoundingMode.DOWN);
        var shares = new ArrayList<BigDecimal>();
        BigDecimal assigned = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (int index = 0; index < participantCount; index++) {
            if (index == participantCount - 1) {
                shares.add(amount.subtract(assigned).setScale(2, RoundingMode.DOWN));
            } else {
                shares.add(baseShare);
                assigned = assigned.add(baseShare);
            }
        }
        return shares;
    }

    private BigDecimal value(BigDecimal amount) {
        return amount == null ? money(BigDecimal.ZERO) : amount;
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
