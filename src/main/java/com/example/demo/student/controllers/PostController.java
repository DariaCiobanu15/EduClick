package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Post;
import com.example.demo.student.services.post.PostRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/course/{courseId}/posts")
public class PostController {
    private final PostRepositoryService postRepositoryService;

    @Autowired
    public PostController(PostRepositoryService postRepositoryService) {
        this.postRepositoryService = postRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Post> getPosts() {
        return postRepositoryService.getPosts();
    }

    @GetMapping(path = "/{postId}")
    public Optional<Post> getPost (@PathVariable("postId") String postId) {
        return postRepositoryService.getPost(postId);
    }

    @PostMapping(path = "/createPost")
    @PreAuthorize("hasRole('ROLE_teacher') || hasRole('ROLE_admin')")
    public void createPost(@RequestBody Post post) {
        postRepositoryService.addNewPost(post);
    }

    @PutMapping(path = "/{postId}/update")
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    public void updatePost (@PathVariable("postId") String postId, @Valid @RequestBody Post post) {
        post.setId(postId);
        postRepositoryService.update(post);
    }

    @PutMapping(path = "/{postId}/delete")
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    public void deletePost(@PathVariable("postId") String postId) {
        postRepositoryService.deletePost(postId);
    }

}
