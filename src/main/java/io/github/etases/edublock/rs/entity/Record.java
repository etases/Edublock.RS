package io.github.etases.edublock.rs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedQuery(name = "Record.findPersonalRecordEntry", query = "Select r.recordEntry FROM Record r WHERE student.id = :student and classroom.id = :classroomId")
public class Record implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Classroom classroom;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;
    @OneToMany(mappedBy = "record")
    private Set<RecordEntry> recordEntry;
    @OneToMany(mappedBy = "record")
    private Set<PendingRecordEntry> pendingRecordEntry;
}
