package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Post;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.post.PostRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/course/posts")
public class PostController {
    private final PostRepositoryService postRepositoryService;
    private final CourseRepositoryService courseRepositoryService;

    @Autowired
    public PostController(PostRepositoryService postRepositoryService, CourseRepositoryService courseRepositoryService) {
        this.postRepositoryService = postRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Post> getPosts() {
        return postRepositoryService.getPosts();
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable("postId") String postId) {
        Optional<Post> post = postRepositoryService.getPost(postId);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/createPost")
    @PreAuthorize("hasRole('ROLE_teacher') || hasRole('ROLE_admin')")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        postRepositoryService.addNewPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
    }

    @PostMapping(path = "/addPost")
    @PreAuthorize("hasRole('ROLE_teacher') || hasRole('ROLE_admin')")
    public Post addPost(@RequestParam("file") Optional<MultipartFile> file,
                        @RequestParam("text") String text,
                        @RequestParam("title") String title,
                        @RequestParam("courseId") String courseId,
                        @RequestParam("day") String day,
                        @RequestParam("month") String month,
                        @RequestParam("year") String year,
                        @RequestParam("type") String type,
                        @RequestParam("isActivity") boolean isActivity,
                        @RequestParam("content") Optional<String> content) throws IOException {
        Post post = new Post();
        post.setText(text);
        post.setTitle(title);
        post.setCourseId(courseId);
        post.setDay(day);
        post.setMonth(month);
        post.setYear(year);
        post.setType(type);
        post.setActivity(isActivity);

        if (file.isPresent() && !file.get().isEmpty()) {
            post.setFileName(file.get().getOriginalFilename());
            post.setContentType(file.get().getContentType());
            post.setContent(file.get().getBytes());
        } else if (content.isPresent() && !content.get().isEmpty()) {
            post.setContent(Base64.getDecoder().decode(content.get()));
        }

        postRepositoryService.addNewPost(post);
        courseRepositoryService.addPostIdToCourse(courseId, post.getId());
        return post;
    }

    @PutMapping(path = "/{postId}/update")
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    public ResponseEntity<String> updatePost(@PathVariable("postId") String postId, @Valid @RequestBody Post post) {
        post.setId(postId);
        postRepositoryService.update(post);
        return ResponseEntity.ok("Post updated successfully.");
    }

    @PutMapping(path = "/{postId}/delete")
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    public ResponseEntity<String> deletePost(@PathVariable("postId") String postId) {
        postRepositoryService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @GetMapping(path = "/{courseId}/getPosts")
    public ResponseEntity<List<Post>> getPostsFromCourse(@PathVariable("courseId") String courseId) {
        System.out.println("KKKKKKK");
        List<String> postsIds = courseRepositoryService.getCourse(courseId).get().getPostsIds();
        List<Post> posts = new ArrayList<>();
        for (String postId : postsIds) {
            Optional<Post> post = postRepositoryService.getPost(postId);
            post.ifPresent(posts::add);
        }
        return ResponseEntity.ok(posts);
    }

//    @GetMapping(path = "/{courseId}/getPostsPreview")
//    public ResponseEntity<List<Post>> getPostsFromCourse(@PathVariable("courseId") String courseId) {
//        List<Post> postsFromCourse = postRepositoryService.getPostsFromCourse(courseId);
//        return ResponseEntity.ok(postsFromCourse);
//    }

}