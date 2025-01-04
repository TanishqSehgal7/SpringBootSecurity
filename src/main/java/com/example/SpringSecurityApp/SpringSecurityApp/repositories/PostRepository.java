package com.example.SpringSecurityApp.SpringSecurityApp.repositories;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}
