package com.example.demo.student.repositories.post;

import com.example.demo.student.componentObj.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, String> {
    Optional<Post> findById(String postId);
}
