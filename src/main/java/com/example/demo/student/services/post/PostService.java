package com.example.demo.student.services.post;

import com.example.demo.student.componentObj.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void create(Post post);
    void update(Post post);
    void delete(Post post);
    Optional<Post> findOne(String id);
    List<Post> findAll();
    Post findById(String id);
}
