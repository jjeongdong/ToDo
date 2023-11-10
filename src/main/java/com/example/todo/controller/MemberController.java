package com.example.todo.controller;

import com.example.todo.config.SecurityUtil;
import com.example.todo.config.jwt.JwtToken;
import com.example.todo.dto.MemberDto;
import com.example.todo.dto.SignInDto;
import com.example.todo.dto.SignUpDto;
import com.example.todo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody @Valid SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username, password);

        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }
}