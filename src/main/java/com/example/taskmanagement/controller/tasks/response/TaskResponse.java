package com.example.taskmanagement.controller.tasks.response;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.enums.Priority;
import com.example.taskmanagement.enums.TaskStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TaskResponse {

    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Long authorId;
    private Long assigneeId;

    public static TaskResponse from(TaskDTO taskDTO) {
        TaskResponse response = new TaskResponse();
        response.title = taskDTO.getTitle();
        response.description = taskDTO.getDescription();
        response.status = taskDTO.getStatus();
        response.priority = taskDTO.getPriority();
        response.authorId = taskDTO.getAuthorId();
        response.assigneeId = taskDTO.getAssigneeId();
        return response;
    }
}
