package com.example.demo.student;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

//import static jdk.jfr.internal.OldObjectSample.emit;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {
    @GeneratedValue(strategy = UNIQUE)
    @Id
    @Field
    private String id;
    @Field
    @NotNull
    private String name;
    @Field
    @NotNull
    private Integer age;

}
