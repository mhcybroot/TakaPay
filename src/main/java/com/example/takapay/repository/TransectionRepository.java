package com.example.takapay.repository;

import com.example.takapay.entity.Transection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransectionRepository extends JpaRepository<Transection,Long> {

    Page<Transection> findBySenderIdOrReceiverId(Long senderId, Long receiverId, Pageable pageable);
}
