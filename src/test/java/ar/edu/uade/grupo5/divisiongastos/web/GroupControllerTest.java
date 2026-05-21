package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.ExpenseService;
import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Member;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;

import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class GroupControllerTest {

    private final GroupService groupService = mock(GroupService.class);
    private final ExpenseService expenseService = mock(ExpenseService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
            new GroupController(groupService, expenseService, new GroupViewMapper(), "ARS")
    ).setViewResolvers((viewName, locale) -> {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()), true);
        }
        return new NoOpView();
    }).build();

    @Test
    void rendersGroupDetail() throws Exception {
        var group = group();
        when(groupService.getGroup(group.id())).thenReturn(group);
        when(expenseService.listExpenses(group.id())).thenReturn(List.of());

        mockMvc.perform(get("/groups/group"))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group", "expenses", "expenseForm", "currency"));
    }

    @Test
    void addsMemberAndRedirects() throws Exception {
        mockMvc.perform(post("/groups/group/members").param("name", "Caro"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/group"));

        verify(groupService).addMember(new GroupId("group"), "Caro");
    }

    @Test
    void addsExpenseAndRedirects() throws Exception {
        mockMvc.perform(post("/groups/group/expenses")
                        .param("payerId", "ana")
                        .param("amount", "100.00")
                        .param("description", "Cena")
                        .param("participantIds", "ana", "beto"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/group"));

        verify(expenseService).addExpense(eq(new GroupId("group")), eq(new MemberId("ana")), any(), eq("Cena"), any());
    }

    private Group group() {
        return new Group(new GroupId("group"), "Viaje", List.of(
                new Member(new MemberId("ana"), "Ana"),
                new Member(new MemberId("beto"), "Beto")
        ));
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
