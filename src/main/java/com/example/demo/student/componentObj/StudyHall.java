package com.example.demo.student.componentObj;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class StudyHall {
    @Id
    @Field
    @GeneratedValue(strategy = UNIQUE)
    private String id = UUID.randomUUID().toString();
    @Field
    @NotNull
    private Integer capacity;
    @Field
    private List<String> bookingIds;
    @Field
    @NotNull
    private String name;
}
