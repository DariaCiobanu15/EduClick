package com.example.demo.student.helperObj;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Module {
    @Field
    @NotNull
    private String name;
    @Field
    @NotNull
    private List<Float> grade;
    @Field
    @NotNull
    private Float avg_grade;
}
