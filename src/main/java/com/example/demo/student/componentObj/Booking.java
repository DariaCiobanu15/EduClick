package com.example.demo.student.componentObj;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Booking {
    @Id
    @Field
    private String id = UUID.randomUUID().toString();
    @Field
    @NotNull
    private String teacherId;
    @Field
    @NotNull
    private String courseId;
    @Field
    private String weekday;
    @Field
    private Integer hour;
    @Field
    private String studyHallId;
    @Field
    private boolean isLab;
    @Field
    private String subGroup;

}
