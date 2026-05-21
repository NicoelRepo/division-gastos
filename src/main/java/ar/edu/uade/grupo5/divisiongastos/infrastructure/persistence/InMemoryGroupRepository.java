package ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.repository.GroupRepository;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGroupRepository implements GroupRepository {

    private final ConcurrentHashMap<GroupId, Group> groups = new ConcurrentHashMap<>();

    @Override
    public Group save(Group group) {
        groups.put(group.id(), group);
        return group;
    }

    @Override
    public Optional<Group> findById(GroupId id) {
        return Optional.ofNullable(groups.get(id));
    }

    @Override
    public List<Group> findAll() {
        return groups.values().stream()
                .sorted(Comparator.comparing(Group::name))
                .toList();
    }
}
