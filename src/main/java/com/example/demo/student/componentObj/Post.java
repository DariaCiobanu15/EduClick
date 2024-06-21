package com.example.demo.student.componentObj;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonInclude;
import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String content;

    @Field
    private Integer gradeWeight;

    @Field
    private List<StudentUploadedContent> studentUploadedContents;

    @JsonIgnore
    public byte[] getDecodedContent() {
        return content != null ? content.getBytes() : null;
    }

    @JsonIgnore
    public void setDecodedContent(byte[] decodedContent) {
        this.content = decodedContent != null ? new String(decodedContent) : null;
    }

    @JsonIgnore
    public byte[] getDecodedContentBytes() {
        return content != null ? Base64.getDecoder().decode(content) : null;
    }

}
