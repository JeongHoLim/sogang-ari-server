package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {

    List<Club> findAllBySection(String section);

    List<Club> findByNameContains(String name);

    Page<Club> findAllBySection(String section,Pageable pageable);

    Page<Club> findByNameContains(String name,Pageable pageable);

}
