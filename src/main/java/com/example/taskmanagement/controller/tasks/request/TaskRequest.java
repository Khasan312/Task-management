package com.example.taskmanagement.controller.tasks.request;

import com.example.taskmanagement.enums.Priority;
import com.example.taskmanagement.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "title не может быть пустым")
    @Size(max = 50, message = "title должен содержать  до 50 символов")
    private String title;
    @NotBlank(message = "description не может быть пустым")
    private String description;
    @NotNull(message = "status не может быть пустым")
    private TaskStatus status;
    @NotNull(message = "priority не может быть пустым")
    private Priority priority;
    private Long assigneeId;
}
