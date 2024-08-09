package com.example.taskmanagement.controller.tasks;

import com.example.taskmanagement.controller.tasks.request.TaskRequest;
import com.example.taskmanagement.controller.tasks.response.TaskResponse;
import com.example.taskmanagement.dto.TaskAndCommentsDTO;
import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;


    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<TaskAndCommentsDTO>> getTasksByAuthor(@PathVariable Long authorId, Pageable pageable) {
        Page<TaskAndCommentsDTO> task = taskService.getTasksByAuthorId(authorId, pageable);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskAndCommentsDTO>> getTasksByAssignee(@PathVariable Long assigneeId, Pageable pageable) {
        Page<TaskAndCommentsDTO> task = taskService.getTasksByAssigneeId(assigneeId, pageable);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(Authentication authentication, @RequestBody TaskDTO taskDTO) {
        User author = (User) authentication.getPrincipal();
        TaskDTO task = taskService.createTask(taskDTO, author);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(Authentication authentication,
                                                   @PathVariable Long taskId,
                                                   @RequestBody TaskRequest request) {
        User author = (User) authentication.getPrincipal();
        TaskDTO task = taskService.updateTask(request, author, taskId);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

    @DeleteMapping("/{authorId}/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long authorId, @PathVariable Long taskId) {
        taskService.deleteTask(authorId, taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
