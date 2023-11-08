package com.example.todo.service;

import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.dto.TodoListDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoDto createTodo(TodoDto todoDto) {
        TodoEntity todoEntity = TodoEntity.builder()
                .title(todoDto.getTitle())
                .completed(false)
                .build();

        todoRepository.save(todoEntity);

        return todoDto;
    }

    @Transactional(readOnly = true)
    public PageResponseDto findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TodoEntity> todoPage = todoRepository.findAll(pageable);

        List<TodoEntity> todoEntityList = todoPage.getContent();

        List<TodoListDto> content = todoEntityList.stream()
                .map(todoEntity -> TodoListDto.builder()
                        .title(todoEntity.getTitle())
                        .completed(todoEntity.getCompleted())
                        .build())
                .collect(Collectors.toList());


        return PageResponseDto.builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(todoPage.getTotalElements())
                .totalPages(todoPage.getTotalPages())
                .last(todoPage.isLast())
                .build();

    }

    @Transactional(readOnly = true)
    public TodoListDto findTodoById(long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return TodoListDto.builder()
                .title(todoEntity.getTitle())
                .completed(todoEntity.getCompleted())
                .build();
    }

    public TodoDto updateTodoById(Long id, TodoDto todoDto) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        todoEntity.setTitle(todoDto.getTitle());

        return TodoDto.builder()
                .title(todoEntity.getTitle())
                .build();
    }


    public void deleteTodoById(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        todoRepository.delete(todoEntity);
    }

    public TodoListDto complete(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        todoEntity.setCompleted(true);

        return TodoListDto.builder()
                .title(todoEntity.getTitle())
                .completed(todoEntity.getCompleted())
                .build();
    }
}