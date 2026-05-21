package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Group(GroupId id, String name, List<Member> members) {

    public Group {
        if (id == null) {
            throw new IllegalArgumentException("Group id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name must not be blank");
        }
        name = name.trim();
        members = List.copyOf(members == null ? List.of() : members);
    }

    public Group addMember(Member member) {
        var updatedMembers = new ArrayList<>(members);
        updatedMembers.add(member);
        return new Group(id, name, updatedMembers);
    }

    public boolean hasMember(MemberId memberId) {
        return members.stream().anyMatch(member -> member.id().equals(memberId));
    }

    public Optional<Member> findMember(MemberId memberId) {
        return members.stream().filter(member -> member.id().equals(memberId)).findFirst();
    }
}
