package com.example.SpringSecurityApp.SpringSecurityApp.services;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.PostDto;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.Post;
import com.example.SpringSecurityApp.SpringSecurityApp.exceptions.ResourceNotFoundException;
import com.example.SpringSecurityApp.SpringSecurityApp.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImplementation implements PostService {

    private final PostRepository postRepository;
    ModelMapper modelMapper;

    public PostServiceImplementation(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PostDto> getAllPosts() {

        List<PostDto> allPosts = postRepository.findAll().
                stream().map(postEntity->
                        modelMapper.map(postEntity, PostDto.class))
                .collect(Collectors.toList());

        if(!allPosts.isEmpty()) {
            return allPosts;
        } else{
            throw new ResourceNotFoundException("No Posts Found!");
        }
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        System.out.println("Post Dto Details: title - "
                + postDto.getTitle() + "\n" + "description - " + postDto.getTitle());

        if(postDto!=null) {
            Post post = postRepository.save(modelMapper.map(postDto, Post.class));
            System.out.println("Post Details: title - "
                    + post.getTitle() + "\n" + "description - " + post.getTitle());
            return modelMapper.map(post,PostDto.class);
        } else {
            throw new ResourceNotFoundException("Cannot Create Post with null data!");
        }
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto inputPost, Long postId) {
        Post olderPost = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id: " + postId)
                );
        inputPost.setId(postId);
        modelMapper.map(inputPost, olderPost);
        Post savedPost = postRepository.save(olderPost);

        return modelMapper.map(savedPost, PostDto.class);
    }
}