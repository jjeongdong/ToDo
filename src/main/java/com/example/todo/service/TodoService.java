package com.example.todo.service;

import com.example.todo.config.SecurityUtil;
import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.dto.TodoListDto;
import com.example.todo.entity.Member;
import com.example.todo.entity.Todo;
import com.example.todo.repository.MemberRepository;
import com.example.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.todo.config.SecurityUtil.getCurrentUsername;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    public TodoDto createTodo(TodoDto todoDto) {

        Member member = memberRepository.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));

        Todo todo = Todo.builder()
                .title(todoDto.getTitle())
                .completed(false)
                .member(member)
                .build();

        todoRepository.save(todo);

        return todoDto;
    }

    @Transactional(readOnly = true)
    public PageResponseDto findAll(int pageNo, int pageSize, String sortBy) {

        Member member = memberRepository.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Todo> todoPage = todoRepository.findAllByMember(pageable, member);

        List<Todo> todoList = todoPage.getContent();

        List<TodoListDto> content = todoList.stream()
                .map(todo -> TodoListDto.builder()
                        .title(todo.getTitle())
                        .completed(todo.getCompleted())
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
        Todo todo = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return TodoListDto.builder()
                .title(todo.getTitle())
                .completed(todo.getCompleted())
                .build();
    }

    @Transactional
    public TodoDto updateTodoById(Long id, TodoDto todoDto) {
        Todo todo = authorizationArticleWriter(id);
        todo.setTitle(todoDto.getTitle());

        return TodoDto.builder()
                .title(todo.getTitle())
                .build();
    }


    @Transactional
    public void deleteTodoById(Long id) {
        Todo todo = authorizationArticleWriter(id);
        todoRepository.delete(todo);
    }


    @Transactional
    public TodoListDto complete(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        todo.setCompleted(true);

        return TodoListDto.builder()
                .title(todo.getTitle())
                .completed(todo.getCompleted())
                .build();
    }


    public Todo authorizationArticleWriter(Long id) {
        String memberName = getCurrentUsername();

        Todo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        if (!todo.getMember().getUsername().equals(memberName)) {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
        return todo;
    }
}