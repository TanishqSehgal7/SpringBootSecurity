package com.example.SpringSecurityApp.SpringSecurityApp.repositories;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE s.userId = :userId")
    Optional<Session> findByUserId(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
