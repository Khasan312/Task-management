package com.example.taskmanagement.controller.comments.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "content не может быть пустым")
    private String content;
}
