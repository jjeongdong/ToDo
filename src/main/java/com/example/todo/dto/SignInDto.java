package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
