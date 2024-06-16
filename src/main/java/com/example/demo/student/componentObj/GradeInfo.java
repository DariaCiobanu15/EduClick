package com.example.demo.student.componentObj;

import org.springframework.data.couchbase.core.mapping.Field;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GradeInfo {

    @Field
    private Integer grade;

    @Field
    private String courseId;

    @Field
    private String postId;

    @Field
    private Integer gradeWeight;

    @Field
    private String postName;
}
