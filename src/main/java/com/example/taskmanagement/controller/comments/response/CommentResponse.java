package com.example.taskmanagement.controller.comments.response;

import com.example.taskmanagement.dto.CommentDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;

    public static CommentResponse from(CommentDTO commentDTO) {
        CommentResponse response = new CommentResponse();
        response.content = commentDTO.getContent();
        response.authorId = commentDTO.getAuthorId();
        response.createdAt = LocalDateTime.now().withNano(0);
        return response;
    }
}
