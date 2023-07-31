package com.example.demo.student.helperObj;

import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UniversityData {
    @Field
    @NotNull
    private Integer year;
    @Field
    @NotNull
    private String group;
}
