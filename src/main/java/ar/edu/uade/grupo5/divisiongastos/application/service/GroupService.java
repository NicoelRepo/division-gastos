package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Member;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.domain.repository.GroupRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(String name) {
        requireText(name, "El nombre del grupo es obligatorio");
        return groupRepository.save(new Group(GroupId.newId(), name, List.of()));
    }

    public Member addMember(GroupId groupId, String name) {
        requireText(name, "El nombre del miembro es obligatorio");
        Group group = getGroup(groupId);
        Member member = new Member(MemberId.newId(), name);
        groupRepository.save(group.addMember(member));
        return member;
    }

    public Group getGroup(GroupId groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new DomainException("Grupo no encontrado"));
    }

    public List<Group> listGroups() {
        return groupRepository.findAll();
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }
}
