package com.example.todo.service;

import com.example.todo.dto.TodoDto;
import com.example.todo.dto.TodoListDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;

    public TodoDto add(TodoDto todoDto) {
        TodoEntity todoEntity = modelMapper.map(todoDto, TodoEntity.class);
        todoEntity.setCompleted(false);
        todoRepository.save(todoEntity);

        return todoDto;
    }

    public List<TodoListDto> findAll() {
        List<TodoEntity> todoEntityList = todoRepository.findAll();

        return todoEntityList.stream()
                .map(data ->modelMapper.map(data, TodoListDto.class))
                .collect(Collectors.toList());
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


}
