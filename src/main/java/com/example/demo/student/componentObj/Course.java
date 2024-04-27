package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Course {
    @Id
    @Field
    private String id = UUID.randomUUID().toString();
    @Field
    @NotNull
    private String name;
    @Field
    @NotNull
    private String teacherId;
    @Field
    private List<String> bookingIds;
    @Field
    private String description;
    @Field
    private List<String> postsIds;
    @Field
    private List<String> labTeachersIds;
    @Field
    private List<String> labStudyHallsIds;
    @Field
    private List<String> studentsIds;

}
