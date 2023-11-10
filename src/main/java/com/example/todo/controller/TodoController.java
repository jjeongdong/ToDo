package com.example.todo.controller;

import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.dto.TodoListDto;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/list")
    public PageResponseDto list(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        return todoService.findAll(pageNo, pageSize, sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoListDto> getTodoById(@PathVariable Long id) {
        TodoListDto todoListDto = todoService.findTodoById(id);
        return ResponseEntity.ok(todoListDto);
    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoDto todoDto) {
        return ResponseEntity.ok(todoService.createTodo(todoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodoById(@PathVariable Long id, @RequestBody @Valid TodoDto todoDto) {
        TodoDto updatedTodoDto = todoService.updateTodoById(id, todoDto);
        return ResponseEntity.ok(updatedTodoDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TodoDto> deleteTodoById(@PathVariable Long id) {
        TodoDto deleteTodoDto = todoService.deleteTodoById(id);
        return ResponseEntity.ok(deleteTodoDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<TodoListDto> complete(@PathVariable Long id) {
        TodoListDto todoListDto = todoService.complete(id);
        return ResponseEntity.ok(todoListDto);
    }


}
