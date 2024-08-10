package com.example.taskmanagement.controller.comments;

import com.example.taskmanagement.controller.comments.request.CommentRequest;
import com.example.taskmanagement.controller.comments.response.CommentResponse;
import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{authorId}/{taskId}")
    public ResponseEntity<CommentResponse> createComment(Authentication authentication,
                                                         @PathVariable Long taskId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {
        User author = (User) authentication.getPrincipal();
        CommentDTO commentDTO = commentService.createComment(commentRequest, author, taskId);
        return ResponseEntity.ok(CommentResponse.from(commentDTO));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(Authentication authentication,
                                                         @PathVariable Long commentId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {
        User author = (User) authentication.getPrincipal();
        CommentDTO commentDTO = commentService.updateComment(commentRequest, author, commentId);
        return ResponseEntity.ok(CommentResponse.fromUpdated(commentDTO));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
