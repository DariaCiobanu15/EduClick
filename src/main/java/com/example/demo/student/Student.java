package com.example.demo.student;

import com.example.demo.student.helperObj.Credentials;
import com.example.demo.student.helperObj.UniversityData;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;


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
    private String userName;
    @Field
    @NotNull
    private Credentials credentials;
    @Field
    @NotNull
    private UniversityData universityData;
    @Field
    private List<Module> moduleList;
}
