package com.example.demo.student.componentObj;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import java.text.SimpleDateFormat;
import java.util.Base64;
import javax.validation.constraints.NotNull;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Post {
    @GeneratedValue(strategy = UNIQUE)
    @Id
    @Field
    private String id;
    @Field
    @NotNull
    private String text;
    @Field
    @NotNull
    private String title;
    @Field
    @NotNull
    private String courseId;
    @Field
    private String day;
    @Field
    private String month;
    @Field
    private String year;
    @Field
    private boolean isActivity;
    @Field
    private String type;
    @Field
    private String fileName;

    @Field
    private String contentType;

    @Field
    private byte[] content = new byte[0];

    public byte[] getContent() {
        return Base64.getDecoder().decode(content);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
