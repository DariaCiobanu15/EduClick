package com.example.demo.student.componentObj;

import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;
import javax.validation.constraints.NotNull;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Credentials {
    @Field
    @NotNull
    private String firstName;
    @Field
    @NotNull
    private String lastName;
}
