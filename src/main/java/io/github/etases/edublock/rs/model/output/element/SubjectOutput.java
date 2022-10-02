package io.github.etases.edublock.rs.model.output.element;

import io.github.etases.edublock.rs.entity.Subject;

public record SubjectOutput(long id, String name) {
    public static SubjectOutput fromEntity(Subject subject) {
        return new SubjectOutput(subject.getId(), subject.getName());
    }
}