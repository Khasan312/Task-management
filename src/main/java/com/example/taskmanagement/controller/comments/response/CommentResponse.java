package com.example.taskmanagement.controller.comments.response;

import com.example.taskmanagement.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private String content;
    private Long authorId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    public static CommentResponse from(CommentDTO commentDTO) {
        CommentResponse response = new CommentResponse();
        response.content = commentDTO.getContent();
        response.authorId = commentDTO.getAuthorId();
        response.createdAt = LocalDateTime.now().withNano(0);
        return response;
    }

    public static CommentResponse fromUpdated(CommentDTO commentDTO) {
        CommentResponse response = new CommentResponse();
        response.content = commentDTO.getContent();
        response.authorId = commentDTO.getAuthorId();
        response.updatedAt = LocalDateTime.now().withNano(0);
        return response;
    }
}
