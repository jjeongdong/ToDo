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

    private final ModelMapper modelMapper;

    public TodoDto add(TodoDto todoDto) {
        TodoEntity todoEntity = modelMapper.map(todoDto, TodoEntity.class);
        todoEntity.setCompleted(false);
        todoRepository.save(todoEntity);

        return todoDto;
    }

    public PageResponseDto findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TodoEntity> todoPage = todoRepository.findAll(pageable);

        List<TodoEntity> todoEntityList = todoPage.getContent();

        List<TodoListDto> content = todoEntityList.stream().map(TodoEntity -> modelMapper.map(TodoEntity, TodoListDto.class)).collect(Collectors.toList());

        return PageResponseDto.builder()
                .content(content)	// todoDtoPage.getContent()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(todoPage.getTotalElements())
                .totalPages(todoPage.getTotalPages())
                .last(todoPage.isLast())
                .build();

    }

    public TodoListDto findById(long id) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        if (todoEntity.isPresent()) {
            return modelMapper.map(todoEntity.get(), TodoListDto.class);
        } else {
            throw new EntityNotFoundException("TodoEntity with ID " + id + " not found");
        }
    }

    public TodoDto updateById(Long id, TodoDto todoDto) {
        Optional<TodoEntity> todoEntityOptional = todoRepository.findById(id);

        if (todoEntityOptional.isPresent()) {
            TodoEntity todoEntity = todoEntityOptional.get();
            todoEntity.setTitle(todoDto.getTitle());

            todoEntity = todoRepository.save(todoEntity);

            return modelMapper.map(todoEntity, TodoDto.class);
        } else {
            throw new EntityNotFoundException("TodoEntity with ID " + id + " not found");
        }
    }

    public void deleteById(Long id) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        if (todoEntity.isPresent()) {
            todoRepository.delete(todoEntity.get());
        } else {
            throw new EntityNotFoundException("TodoEntity with ID " + id + " not found");
        }
    }

    public TodoListDto complete(Long id) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        if (todoEntity.isPresent()) {
            todoEntity.get().setCompleted(true);
            todoRepository.save(todoEntity.get());
            return modelMapper.map(todoEntity.get(), TodoListDto.class);
        } else {
            throw new EntityNotFoundException("TodoEntity with ID " + id + " not found");
        }
    }


}
