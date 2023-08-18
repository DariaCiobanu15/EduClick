package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.List;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Course {
    @GeneratedValue(strategy = UNIQUE)
    @Id
    @Field
    private String id;
    @Field
    @NotNull
    private String name;
    @Field
    @NotNull
    private String studyHall;
    @Field
    @NotNull
    private String hour;
//    @Field
//    @NotNull
//    private List<String> weekdays;
    @Field
    @NotNull
//    private Teacher teacher;
    private String teacher;
}
