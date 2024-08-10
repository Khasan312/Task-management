package com.example.taskmanagement;

import com.example.taskmanagement.controller.tasks.request.TaskRequest;
import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.enums.Priority;
import com.example.taskmanagement.enums.TaskStatus;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(userRepository, taskRepository, commentRepository);
    }

    @Test
    public void testCreateTask() {
        User author = new User();
        author.setId(1L);

        User assignee = new User();
        assignee.setId(2L);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Task Description");
        taskRequest.setStatus(TaskStatus.WAITING);
        taskRequest.setPriority(Priority.MEDIUM);
        taskRequest.setAssigneeId(2L);

        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(userRepository.findById(taskRequest.getAssigneeId())).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any())).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1L);
            return task;
        });

        TaskDTO result = taskService.createTask(taskRequest, author);

        assertEquals(taskRequest.getTitle(), result.getTitle());
        assertEquals(taskRequest.getDescription(), result.getDescription());
    }


    @Test
    public void testGetTask() {
        Long authorId = 1L;
        Long taskId = 1L;

        User author = new User();
        author.setId(authorId);

        Task task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Task Description")
                .status(TaskStatus.WAITING)
                .priority(Priority.MEDIUM)
                .authorId(authorId)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDTO taskDTO = taskService.getTask(authorId, taskId);

        assertEquals("Test Task", taskDTO.getTitle());
        verify(userRepository, times(1)).findById(authorId);
        verify(taskRepository, times(1)).findById(taskId);
    }


    @Test
    public void testGetTask_NotFound() {
        Long authorId = 1L;
        Long taskId = 1L;

        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTask(authorId, taskId));
    }
}
