package com.example.demo.student.services.post;

import com.example.demo.student.componentObj.Post;
import com.example.demo.student.componentObj.StudentUploadedContent;
import com.example.demo.student.repositories.post.PostRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Qualifier("PostRepositoryService")
public class PostRepositoryService {
    private final PostRepository postRepository;

    @Autowired
    public PostRepositoryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> getPost(String id){
        return postRepository.findById(id);
    }

    public void create(Post post) {
        postRepository.save(post);
    }

    public void update(Post post) {
        postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void addNewPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(String id) {
        boolean exists = postRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("Post doesn't exist!");
        }
        postRepository.deleteById(id);
    }

    public void addStudentUploadedContentToPost(String postId, StudentUploadedContent studentUploadedContent) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("Post doesn't exist!"));
        List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
        if(post.getStudentUploadedContents() == null) {
            studentUploadedContents = List.of(studentUploadedContent);
        } else {
            studentUploadedContents.add(studentUploadedContent);
        }
        post.setStudentUploadedContents(studentUploadedContents);
        postRepository.save(post);

    }

    public List<Post> getPostsByCourseId(String courseId) {
        return postRepository.findPostsByCourseId(courseId);
    }
}
