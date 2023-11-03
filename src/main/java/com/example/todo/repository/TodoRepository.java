package com.example.todo.repository;

import com.example.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    Page<TodoEntity> findAll(Pageable pageable);
}
