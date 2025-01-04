package com.example.SpringSecurityApp.SpringSecurityApp.controllers;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.PostDto;
import com.example.SpringSecurityApp.SpringSecurityApp.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<PostDto> createNewPost(@RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.FOUND);
    }

    @PutMapping(path = "/{postId}")
    public ResponseEntity<PostDto> updatePostDto(@RequestBody PostDto postDto, @PathVariable Long postId) {
        return new ResponseEntity<>(postService.updatePost(postDto, postId), HttpStatus.OK);
    }
}