package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Transfer;
import ar.edu.uade.grupo5.divisiongastos.domain.service.BalanceCalculator;
import ar.edu.uade.grupo5.divisiongastos.domain.service.SettlementSolver;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {

    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final BalanceCalculator balanceCalculator = new BalanceCalculator();
    private final SettlementSolver settlementSolver = new SettlementSolver();

    public SettlementService(GroupService groupService, ExpenseService expenseService) {
        this.groupService = groupService;
        this.expenseService = expenseService;
    }

    public List<Transfer> computeSettlement(GroupId groupId) {
        Group group = groupService.getGroup(groupId);
        var expenses = expenseService.listExpenses(groupId);
        var balance = balanceCalculator.calculate(group, expenses);
        return settlementSolver.solve(balance);
    }
}
