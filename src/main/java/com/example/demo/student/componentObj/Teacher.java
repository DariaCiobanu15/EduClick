package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Teacher {
    @Field
    @NotNull
    private String username;
    @Field
    @NotNull
    private String firstName;
    @Field
    @NotNull
    private String lastName;
    @Field
    @NotNull
    private String course_name;
}
