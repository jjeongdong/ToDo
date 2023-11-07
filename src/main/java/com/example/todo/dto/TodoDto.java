package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.ErrorResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {

    @NotBlank
//    @Length(min = 2)
    private String title;

}
