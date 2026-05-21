package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HomeControllerTest {

    private final GroupService groupService = mock(GroupService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(groupService)).build();

    @Test
    void rendersGroups() throws Exception {
        when(groupService.listGroups()).thenReturn(List.of(new Group(new GroupId("group"), "Viaje", List.of())));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("groups"));
    }

    @Test
    void redirectsToCreatedGroup() throws Exception {
        when(groupService.createGroup("Viaje")).thenReturn(new Group(new GroupId("group"), "Viaje", List.of()));

        mockMvc.perform(post("/groups").param("name", "Viaje"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/group"));
    }
}
