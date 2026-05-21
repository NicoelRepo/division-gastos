package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.application.service.SettlementService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Member;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Transfer;

import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class SettlementControllerTest {

    private final GroupService groupService = mock(GroupService.class);
    private final SettlementService settlementService = mock(SettlementService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
            new SettlementController(groupService, settlementService, new GroupViewMapper(), "ARS")
    ).setViewResolvers((viewName, locale) -> {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()), true);
        }
        return new NoOpView();
    }).build();

    @Test
    void rendersSettlement() throws Exception {
        var groupId = new GroupId("group");
        var anaId = new MemberId("ana");
        var betoId = new MemberId("beto");
        var group = new Group(groupId, "Viaje", List.of(
                new Member(anaId, "Ana"),
                new Member(betoId, "Beto")
        ));
        when(groupService.getGroup(groupId)).thenReturn(group);
        when(settlementService.computeSettlement(groupId))
                .thenReturn(List.of(new Transfer(betoId, anaId, new BigDecimal("50.00"))));

        mockMvc.perform(get("/groups/group/settlement"))
                .andExpect(status().isOk())
                .andExpect(view().name("settlement"))
                .andExpect(model().attributeExists("group", "transfers", "currency"));
    }

    private static class NoOpView extends AbstractView {

        @Override
        protected void renderMergedOutputModel(
                Map<String, Object> model,
                HttpServletRequest request,
                HttpServletResponse response
        ) {
            response.setStatus(200);
        }
    }
}
