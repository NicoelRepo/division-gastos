package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;
import ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence.InMemoryGroupRepository;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupServiceTest {

    private final GroupService groupService = new GroupService(new InMemoryGroupRepository());

    @Test
    void createsGroupAndAddsMember() {
        var group = groupService.createGroup("Viaje");
        var member = groupService.addMember(group.id(), "Ana");

        var stored = groupService.getGroup(group.id());

        assertThat(stored.name()).isEqualTo("Viaje");
        assertThat(stored.members()).contains(member);
    }

    @Test
    void rejectsBlankGroupName() {
        assertThatThrownBy(() -> groupService.createGroup(" "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("grupo");
    }
}
