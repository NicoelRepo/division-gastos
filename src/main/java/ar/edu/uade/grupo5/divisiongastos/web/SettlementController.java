package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.application.service.SettlementService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SettlementController {

    private final GroupService groupService;
    private final SettlementService settlementService;
    private final GroupViewMapper groupViewMapper;
    private final String currency;

    public SettlementController(
            GroupService groupService,
            SettlementService settlementService,
            GroupViewMapper groupViewMapper,
            @Value("${app.currency:ARS}") String currency
    ) {
        this.groupService = groupService;
        this.settlementService = settlementService;
        this.groupViewMapper = groupViewMapper;
        this.currency = currency;
    }

    @GetMapping("/groups/{id}/settlement")
    public String settlement(@PathVariable String id, Model model) {
        var groupId = new GroupId(id);
        var group = groupService.getGroup(groupId);
        var transfers = settlementService.computeSettlement(groupId).stream()
                .map(transfer -> groupViewMapper.toTransferView(group, transfer))
                .toList();
        model.addAttribute("group", group);
        model.addAttribute("transfers", transfers);
        model.addAttribute("currency", currency);
        return "settlement";
    }
}
