package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {

    @NotBlank
    private String title;

}
