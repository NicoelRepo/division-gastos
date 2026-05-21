package ar.edu.uade.grupo5.divisiongastos.domain.repository;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {

    Group save(Group group);

    Optional<Group> findById(GroupId id);

    List<Group> findAll();
}
