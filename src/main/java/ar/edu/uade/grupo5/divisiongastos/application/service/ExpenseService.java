package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.domain.repository.ExpenseRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupService groupService;

    public ExpenseService(ExpenseRepository expenseRepository, GroupService groupService) {
        this.expenseRepository = expenseRepository;
        this.groupService = groupService;
    }

    public Expense addExpense(
            GroupId groupId,
            MemberId payerId,
            BigDecimal amount,
            String description,
            Set<MemberId> participantIds
    ) {
        Group group = groupService.getGroup(groupId);
        validateExpense(group, payerId, amount, description, participantIds);
        Expense expense = Expense.create(groupId, payerId, amount, description, participantIds);
        return expenseRepository.save(expense);
    }

    public List<Expense> listExpenses(GroupId groupId) {
        groupService.getGroup(groupId);
        return expenseRepository.findByGroupId(groupId);
    }

    private void validateExpense(
            Group group,
            MemberId payerId,
            BigDecimal amount,
            String description,
            Set<MemberId> participantIds
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("El monto debe ser positivo");
        }
        if (description == null || description.isBlank()) {
            throw new DomainException("La descripción es obligatoria");
        }
        if (payerId == null || !group.hasMember(payerId)) {
            throw new DomainException("El pagador debe pertenecer al grupo");
        }
        if (participantIds == null || participantIds.isEmpty()) {
            throw new DomainException("Debe seleccionar al menos un participante");
        }
        boolean allParticipantsBelongToGroup = participantIds.stream().allMatch(group::hasMember);
        if (!allParticipantsBelongToGroup) {
            throw new DomainException("Todos los participantes deben pertenecer al grupo");
        }
    }
}
