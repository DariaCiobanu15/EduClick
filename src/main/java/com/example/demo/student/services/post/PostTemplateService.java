package com.example.demo.student.services.post;

import com.example.demo.student.componentObj.Post;
import com.example.demo.student.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

public class PostTemplateService implements PostService {
    private static final String DESIGN_DOC = "post";
    private CouchbaseTemplate template;

    @Override
    public void create(Post post) {
        template.insertById(Post.class).one(post);
    }

    @Override
    public void update(Post post) {
        template.removeById(Post.class).one(post.getId());
    }

    @Override
    public void delete(Post post) {
        template.removeById(Post.class).one(post.getId());
    }

    @Override
    public Optional<Post> findOne(String id) {
        return Optional.of(template.findById(Post.class).one(id));
    }

    @Override
    public List<Post> findAll() {
        // findAll but conversing content field from type [java.lang.String] to type [byte]
//        for (Post post : template.findByQuery(Post.class).all()) {
//            post.setContent(post.getContent());
//        }
        return template.findByQuery(Post.class).all();
    }

    @Override
    public Post findById(String id) {
        if(template.findById(Post.class).one(id) == null) {
            System.out.println("Post doesn't exist!");
            throw new IllegalStateException("Post doesn't exist!");
        } else {
            System.out.println("Post exists!");
            Post post = template.findById(Post.class).one(id);
            return post;
        }
    }

    public List<Post> myFindAll() {
        // findAll but conversing content field from type [java.lang.String] to type [byte]
//        for (Post post : template.findByQuery(Post.class).all()) {
//            post.setContent(post.getContent());
//        }
        return template.findByQuery(Post.class).all();
    }


    @Autowired
    public void setCouchbaseTemplate(CouchbaseTemplate template) {
        this.template = template;
    }
}