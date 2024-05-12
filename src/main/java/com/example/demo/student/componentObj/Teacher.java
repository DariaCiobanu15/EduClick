package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Teacher {
    @GeneratedValue(strategy = UNIQUE)
    @Id
    @Field
    private String id;
    @Field
    @NotNull
    private String username;
    @Field
    @NotNull
    private String password;
    @Field
    @NotNull
    private String email;
    @Field
    @NotNull
    private Credentials credentials;
    @Field
    private List<String> courseIds;
    @Field
    private List<String> labIds;
}
