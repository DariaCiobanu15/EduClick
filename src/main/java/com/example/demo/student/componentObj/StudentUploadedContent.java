package com.example.demo.student.componentObj;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentUploadedContent {

    @Field
    @NotNull
    private String studentId;

    @Field
    @NotNull
    private String content;

    @Field
    private String fileName;

    @Field
    @NotNull
    private String contentType;

    @Field
    private Integer grade;

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
