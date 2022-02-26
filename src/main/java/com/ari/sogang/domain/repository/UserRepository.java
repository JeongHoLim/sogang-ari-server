package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
