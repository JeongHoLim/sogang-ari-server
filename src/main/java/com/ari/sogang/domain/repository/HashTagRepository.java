package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag,Long> {

    List<HashTag> findAllById(List<Long> ids);

    List<HashTag> findAllByName(List<String> names);
}
