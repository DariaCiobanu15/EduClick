package com.example.demo.student.services.post;

import com.example.demo.student.componentObj.Post;
import com.example.demo.student.repositories.post.PostRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Base64;
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
        System.out.println("PostRepositoryService.getPost");
//        System.out.println(postRepository.findById(id));
//        return postRepository.findById(id);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            if (post.get().getContent() != null) {
                post.get().setBase64Content(Base64.getEncoder().encodeToString(post.get().getContent()));
            }
        }
        System.out.println(post);
        return post;
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
        List<Post> posts = postRepository.findAll();
        System.out.println("PostRepositoryService.getPosts");
        posts.forEach(post -> {
            if (post.getContent() != null) {
                post.setBase64Content(Base64.getEncoder().encodeToString(post.getContent()));
            }
        });
        System.out.println(posts);
        return posts;
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


}