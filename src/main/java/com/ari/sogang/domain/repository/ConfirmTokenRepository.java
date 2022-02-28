package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken,Long> {
    Optional<ConfirmToken> findByToken(String token);
}
