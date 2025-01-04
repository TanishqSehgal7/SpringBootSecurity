package com.example.SpringSecurityApp.SpringSecurityApp.services;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.PostDto;
import java.util.List;


public interface PostService {

    List<PostDto> getAllPosts();

    PostDto createPost(PostDto postDto);

    PostDto getPostById(Long postId);

    PostDto updatePost(PostDto postDto, Long postId);
}