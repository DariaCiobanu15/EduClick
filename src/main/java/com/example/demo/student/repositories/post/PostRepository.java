package com.example.demo.student.repositories.post;

import com.example.demo.student.componentObj.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, String>{

    @Override
    Optional<Post> findById(String postId);


    // findAll but conversing content field from type [java.lang.String] to type [byte]
    public List<Post> findAll();
}