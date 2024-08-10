package com.example.taskmanagement.service;

import com.example.taskmanagement.controller.comments.request.CommentRequest;
import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.entity.Comment;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public CommentDTO createComment(CommentRequest request, User author, Long taskId) {
        Comment comment = Comment.builder()
                .content(request.getContent())
                .authorId(author.getId())
                .taskId(taskId)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        Comment savedComment = commentRepository.save(comment);
        return convertDTO(savedComment);
    }

    public CommentDTO updateComment(CommentRequest request, User author, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getAuthorId().equals(author.getId())) {
            throw new NotFoundException("You are not allowed to update this comment");
        }
        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now().withNano(0));
        commentRepository.save(comment);
        return convertDTO(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        return commentRepository.findByTaskId(taskId).stream()
                .map(comment -> CommentDTO.builder()
                        .content(comment.getContent())
                        .authorId(comment.getAuthorId())
                        .taskId(comment.getTaskId())
                        .build()).toList();
    }

    private CommentDTO convertDTO(Comment comment) {
        return CommentDTO.builder()
                .content(comment.getContent())
                .authorId(comment.getAuthorId())
                .taskId(comment.getTaskId())
                .build();
    }
}
