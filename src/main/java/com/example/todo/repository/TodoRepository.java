package com.example.todo.repository;

import com.example.todo.entity.Member;
import com.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findAllByMember(Pageable pageable, Member member);

    Optional<Todo> findByIdAndMember(Long id, Member member);
}
