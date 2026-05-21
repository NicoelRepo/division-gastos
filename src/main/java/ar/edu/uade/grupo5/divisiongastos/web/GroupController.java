package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.ExpenseService;
import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashSet;

@Controller
public class GroupController {

    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final GroupViewMapper groupViewMapper;
    private final String currency;

    public GroupController(
            GroupService groupService,
            ExpenseService expenseService,
            GroupViewMapper groupViewMapper,
            @Value("${app.currency:ARS}") String currency
    ) {
        this.groupService = groupService;
        this.expenseService = expenseService;
        this.groupViewMapper = groupViewMapper;
        this.currency = currency;
    }

    @GetMapping("/groups/{id}")
    public String groupDetail(@PathVariable String id, Model model) {
        var groupId = new GroupId(id);
        var group = groupService.getGroup(groupId);
        var expenses = expenseService.listExpenses(groupId).stream()
                .map(expense -> groupViewMapper.toExpenseView(group, expense))
                .toList();
        model.addAttribute("group", group);
        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseForm", new ExpenseForm());
        model.addAttribute("currency", currency);
        return "group";
    }

    @PostMapping("/groups/{id}/members")
    public String addMember(
            @PathVariable String id,
            @RequestParam String name,
            RedirectAttributes redirectAttributes
    ) {
        try {
            groupService.addMember(new GroupId(id), name);
        } catch (DomainException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/groups/" + id;
    }

    @PostMapping("/groups/{id}/expenses")
    public String addExpense(
            @PathVariable String id,
            @ModelAttribute ExpenseForm expenseForm,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var participantIds = expenseForm.getParticipantIds().stream()
                    .map(MemberId::new)
                    .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
            expenseService.addExpense(
                    new GroupId(id),
                    new MemberId(expenseForm.getPayerId()),
                    expenseForm.getAmount(),
                    expenseForm.getDescription(),
                    participantIds
            );
        } catch (DomainException | IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/groups/" + id;
    }
}
