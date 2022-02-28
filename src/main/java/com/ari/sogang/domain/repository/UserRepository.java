package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByStudentId(String studentId);


    List<User> findAll();

    Optional<User> findByEmail(String email);
}
