package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;

import java.text.SimpleDateFormat;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Post {
    @Field
    @NotNull
    private String text;
    @Field
    @NotNull
    private String title;
    @Field
    private String day;
    @Field
    private String month;
    @Field
    private String year;
}
