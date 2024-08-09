package com.example.taskmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class TaskAndCommentsDTO {
    private TaskDTO task;
    private List<CommentDTO> comments;
}
