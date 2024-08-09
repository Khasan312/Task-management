package com.example.taskmanagement.service;


import com.example.taskmanagement.controller.tasks.request.TaskRequest;
import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.dto.TaskAndCommentsDTO;
import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.entity.Comment;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.enums.Priority;
import com.example.taskmanagement.enums.TaskStatus;
import com.example.taskmanagement.exception.AccessDeniedException;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;


    public Page<TaskAndCommentsDTO> getTasksByAuthorId(Long authorId, Pageable pageable) {
        return taskRepository.findByAuthorId(authorId, pageable).map(this::convertToTaskAndCommentsDTO);
    }

    public Page<TaskAndCommentsDTO> getTasksByAssigneeId(Long id, Pageable pageable) {
        return taskRepository.findByAssigneeId(id, pageable).map(this::convertToTaskAndCommentsDTO);
    }

    public TaskDTO getTask(Long authorId, Long taskId) {
        userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getAuthorId().equals(authorId)) {
            throw new NotFoundException("You are not allowed to access this task");
        }
        return convertToDTO(task);
    }

    public TaskDTO createTask(TaskDTO taskDTO, User author) {
        User assignee = null;
        if (taskDTO.getAssigneeId() != null) {
            assignee = userRepository.findById(taskDTO.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.WAITING)
                .priority(taskDTO.getPriority() != null ? taskDTO.getPriority() : Priority.MEDIUM)
                .authorId(author.getId())
                .assigneeId(assignee != null ? assignee.getId() : author.getId())
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTask(TaskRequest taskDTO, User author, Long taskId) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!existingTask.getAuthorId().equals(author.getId()) &&
                !existingTask.getAssigneeId().equals(author.getId())) {
            throw new AccessDeniedException("You are not allowed to update this task");
        }
        User assignee = null;
        if (taskDTO.getAssigneeId() != null) {
            assignee = userRepository.findById(taskDTO.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
        }
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.WAITING);
        existingTask.setPriority(taskDTO.getPriority() != null ? taskDTO.getPriority() : Priority.MEDIUM);
        existingTask.setAuthorId(author.getId());
        existingTask.setAssigneeId(assignee != null ? assignee.getId() : author.getId());
        existingTask.setUpdatedAt(LocalDateTime.now().withNano(0));

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long author, Long taskId) {


        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getAuthorId().equals(author)) {
            throw new NotFoundException("You are not allowed to delete this task");
        }

        taskRepository.delete(task);
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .authorId(task.getAuthorId())
                .assigneeId(task.getAssigneeId())
                .build();
    }

    private TaskAndCommentsDTO convertToTaskAndCommentsDTO(Task task) {
        List<CommentDTO> comments = commentRepository.findByTaskId(task.getId()).stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());

        return TaskAndCommentsDTO.builder()
                .task(convertToDTO(task))
                .comments(comments)
                .build();
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .content(comment.getContent())
                .authorId(comment.getAuthorId())
                .taskId(comment.getTaskId())
                .build();
    }
}
