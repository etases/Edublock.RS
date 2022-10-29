package io.github.etases.edublock.rs.model.output.element;

import io.github.etases.edublock.rs.entity.Classroom;
import io.github.etases.edublock.rs.entity.Profile;
import io.github.etases.edublock.rs.model.fabric.ClassRecord;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.function.LongFunction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomOutput {
    long id;
    String name;
    int grade;
    int year;
    AccountWithProfileOutput homeroomTeacher;

    public static ClassroomOutput fromEntity(Classroom classroom, LongFunction<Profile> profileFunction) {
        return new ClassroomOutput(
                classroom.getId(),
                classroom.getName(),
                classroom.getGrade(),
                classroom.getYear(),
                AccountWithProfileOutput.fromEntity(classroom.getHomeroomTeacher(), profileFunction)
        );
    }

    public static ClassroomOutput fromFabricModel(long classId, ClassRecord classRecord) {
        var classroom = new ClassroomOutput();
        classroom.setId(classId);
        classroom.setYear(classRecord.getYear());
        classroom.setGrade(classRecord.getGrade());
        classroom.setName(classRecord.getClassName());
        classroom.setHomeroomTeacher(new AccountWithProfileOutput());
        return classroom;
    }
}