package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.GradeInfo;
import com.example.demo.student.componentObj.Post;
import com.example.demo.student.componentObj.StudentUploadedContent;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.post.PostRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/course/posts")
public class PostController {
    private final PostRepositoryService postRepositoryService;
    private final CourseRepositoryService courseRepositoryService;
    private final StudentRepositoryService studentRepositoryService;

    @Autowired
    public PostController(PostRepositoryService postRepositoryService, CourseRepositoryService courseRepositoryService, StudentRepositoryService studentRepositoryService) {
        this.postRepositoryService = postRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
        this.studentRepositoryService = studentRepositoryService;
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
    public Post addPost(@RequestParam("text") String text,
                        @RequestParam("title") String title,
                        @RequestParam("courseId") String courseId,
                        @RequestParam("day") String day,
                        @RequestParam("month") String month,
                        @RequestParam("year") String year,
                        @RequestParam("type") String type,
                        @RequestParam("isActivity") boolean isActivity,
                        @RequestParam("gradeWeight") Optional<Integer> gradeWeight,
                        @RequestParam("contentType") Optional<String> contentType,
                        @RequestParam("fileName") Optional<String> fileName,
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
        post.setGradeWeight(gradeWeight.orElse(null));
        post.setStudentUploadedContents(new ArrayList<>());

        if (content.isPresent() && !content.get().isEmpty()) {
            post.setFileName(fileName.orElse(null));
            System.out.println(post.getFileName());
            post.setContentType(contentType.orElse(null));
            post.setContent(content.get());
            System.out.println(post.getContent());
            System.out.println(post.getContent().getClass());
        } else {
            post.setContent(null);
        }

        System.out.println(post);
        postRepositoryService.addNewPost(post);
        courseRepositoryService.addPostIdToCourse(courseId, post.getId());

        if (post.isActivity()) {
            List<String> studentsIds = courseRepositoryService.getCourse(courseId).orElseThrow(() -> new IllegalStateException("Course doesn't exist!")).getStudentsIds();
            for (String studentId : studentsIds) {
                studentRepositoryService.addActivityToStudent(studentId, post.getId());
            }
        }
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
        List<String> postsIds = courseRepositoryService.getCourse(courseId).orElseThrow(() -> new IllegalStateException("Course doesn't exist!")).getPostsIds();
        List<Post> posts = new ArrayList<>();
        for (String postId : postsIds) {
            postRepositoryService.getPost(postId).ifPresent(posts::add);
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping(path = "/{courseId}/{postId}/getContent")
    public ResponseEntity<ByteArrayResource> getPostContent(@PathVariable("courseId") String courseId, @PathVariable("postId") String postId) {
        Optional<Post> optionalPost = postRepositoryService.getPost(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            byte[] content = post.getDecodedContentBytes();
            if (content != null) {
                ByteArrayResource resource = new ByteArrayResource(content);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(post.getContentType()));
                headers.setContentDisposition(ContentDisposition.inline().filename(post.getFileName()).build());
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(content.length)
                        .body(resource);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/{studentId}/getActivities")
    public ResponseEntity<List<Post>> getActivities(@PathVariable("studentId") String studentId) {
        List<String> activitiesIds = studentRepositoryService.getStudent(studentId).orElseThrow(() -> new IllegalStateException("Student doesn't exist!")).getActivitiesIds();
        List<Post> activities = new ArrayList<>();
        for (String activityId : activitiesIds) {
            postRepositoryService.getPost(activityId).ifPresent(activities::add);
        }
        return ResponseEntity.ok(activities);
    }

    @GetMapping(path = "/{studentId}/getUndoneActivities")
    public ResponseEntity<List<Post>> getUndoneActivities(@PathVariable("studentId") String studentId) {
        List<String> activitiesIds = studentRepositoryService.getStudent(studentId).orElseThrow(() -> new IllegalStateException("Student doesn't exist!")).getActivitiesIds();
        List<Post> activities = new ArrayList<>();
        for (String activityId : activitiesIds) {
            System.out.println(activityId);
            Post post = postRepositoryService.getPost(activityId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
            List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
            if (studentUploadedContents == null || studentUploadedContents.isEmpty()) {
                activities.add(post);
            } else {
                boolean isDone = studentUploadedContents.stream()
                        .noneMatch(content -> content.getStudentId().equals(studentId));
                System.out.println(isDone);
                if (isDone) {
                    activities.add(post);
                }
            }
        }
        return ResponseEntity.ok(activities);
    }

    @PostMapping(path = "/{studentId}/UploadContentForActivity")
    public ResponseEntity<String> uploadContentForActivity(@PathVariable("studentId") String studentId,
                                                           @RequestParam("postId") String postId,
                                                           @RequestParam("content") String content,
                                                           @RequestParam("contentType") String contentType,
                                                           @RequestParam("fileName") String fileName
                                                           ) throws IOException {
        Post post = postRepositoryService.getPost(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
        if (!post.isActivity()) {
            throw new IllegalStateException("Post is not an activity!");
        }
        StudentUploadedContent studentUploadedContent = new StudentUploadedContent();
        studentUploadedContent.setStudentId(studentId);
        if(content != null && !content.isEmpty()) {
            studentUploadedContent.setContent(content);
            studentUploadedContent.setContentType(contentType);
            studentUploadedContent.setFileName(fileName);

        } else {
            studentUploadedContent.setContent(null);
        }
        postRepositoryService.addStudentUploadedContentToPost(postId, studentUploadedContent);
        return ResponseEntity.ok("Content uploaded successfully.");

    }

    @GetMapping(path = "/{postId}/{studentId}/getStudentUploadedContent")
    public ResponseEntity<ByteArrayResource> getStudentUploadedContent(@PathVariable("postId") String postId, @PathVariable("studentId") String studentId) {
        Post post = postRepositoryService.getPost(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
        if (!post.isActivity()) {
            throw new IllegalStateException("Post is not an activity!");
        }
        List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
        if (studentUploadedContents == null || studentUploadedContents.isEmpty()) {
            throw new IllegalStateException("No content uploaded for this activity!");
        }
        StudentUploadedContent studentUploadedContent = studentUploadedContents.stream()
                .filter(content -> content.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No content uploaded for this activity by this student!"));
        byte[] content = studentUploadedContent.getDecodedContentBytes();
        if (content != null) {
            ByteArrayResource resource = new ByteArrayResource(content);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(studentUploadedContent.getContentType()));
            headers.setContentDisposition(ContentDisposition.inline().filename(studentUploadedContent.getFileName()).build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(content.length)
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_teacher') || hasRole('ROLE_admin')")
    @GetMapping(path = "/{postId}/getAllUploadedContents")
    public ResponseEntity<List<StudentUploadedContent>> getAllUploadedContents(@PathVariable("postId") String postId) {
        Post post = postRepositoryService.getPost(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
        if (!post.isActivity()) {
            throw new IllegalStateException("Post is not an activity!");
        }
        List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
        if (studentUploadedContents == null || studentUploadedContents.isEmpty()) {
            throw new IllegalStateException("No content uploaded for this activity!");
        }
        return ResponseEntity.ok(studentUploadedContents);
    }

    @PutMapping(path = "/{postId}/{studentId}/gradeContent")
    @PreAuthorize("hasRole('ROLE_teacher') || hasRole('ROLE_admin')")
    public ResponseEntity<String> gradeContent(@PathVariable("postId") String postId, @PathVariable("studentId") String studentId, @RequestParam("grade") String grade) {
        Post post = postRepositoryService.getPost(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
        if (!post.isActivity()) {
            throw new IllegalStateException("Post is not an activity!");
        }
        List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
        if (studentUploadedContents == null || studentUploadedContents.isEmpty()) {
            throw new IllegalStateException("No content uploaded for this activity!");
        }
        StudentUploadedContent studentUploadedContent = studentUploadedContents.stream()
                .filter(content -> content.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No content uploaded for this activity by this student!"));
        studentUploadedContent.setGrade(Integer.valueOf(grade));
        postRepositoryService.update(post);
        return ResponseEntity.ok("Content graded successfully.");
    }


    @GetMapping(path = "/{studentId}/getGrades")
    public List<GradeInfo> getGrades(@PathVariable("studentId") String studentId, @RequestParam("courseId") String courseId) {
        List<String> postsIds = courseRepositoryService.getCourse(courseId).orElseThrow(() -> new IllegalStateException("Course doesn't exist!")).getPostsIds();
        List<GradeInfo> grades = new ArrayList<>();
        for (String postId : postsIds) {
            Post post = postRepositoryService.getPost(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
            if (post.isActivity()) {
                List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
                if (studentUploadedContents != null && !studentUploadedContents.isEmpty()) {
                    StudentUploadedContent studentUploadedContent = studentUploadedContents.stream()
                            .filter(content -> content.getStudentId().equals(studentId))
                            .findFirst()
                            .orElse(null);
                    if (studentUploadedContent != null) {
                        GradeInfo gradeInfo = new GradeInfo();
                        gradeInfo.setGrade(studentUploadedContent.getGrade());
                        gradeInfo.setCourseId(courseId);
                        gradeInfo.setPostId(postId);
                        gradeInfo.setGradeWeight(post.getGradeWeight());
                        gradeInfo.setPostName(post.getTitle());
                        grades.add(gradeInfo);
                    }
                }
            }
        }
        return grades;
    }
}
