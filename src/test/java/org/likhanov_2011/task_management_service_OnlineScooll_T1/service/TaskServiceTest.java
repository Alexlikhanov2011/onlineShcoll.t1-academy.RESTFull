package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.DuplicateTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.InvalidTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.TaskNotFoundException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka.TaskProducer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.repository.TaskRepository;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskProducer taskProducer;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    private final Long TASK_ID = 1L;
    private final Long USER_ID = 100L;
    private final String TASK_TITLE = "Test Task";


    @Test
    void update_NonExistingTask_ThrowsException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.update(new TaskDTO(TASK_ID, TASK_TITLE, "Description", USER_ID,"NEW")));
    }


    private TaskDTO createTestTaskDTO() {
        return new TaskDTO(TASK_ID, TASK_TITLE, "Description", USER_ID,"NEW");
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        task.setStatus("NEW");
        task.setUserId(USER_ID);
        return task;
    }

    @Test
    void add_shouldThrowWhenTaskDtoIsNull() {
        assertThrows(InvalidTaskException.class,
                () -> taskService.add(null));
    }

    @Test
    void add_shouldThrowWhenTitleIsBlank() {
        TaskDTO dto = createTestTaskDTO();
        dto.setTitle(" ");

        assertThrows(InvalidTaskException.class,
                () -> taskService.add(dto));
    }

    @Test
    void add_shouldThrowWhenUserIsNull() {
        TaskDTO dto = createTestTaskDTO();
        dto.setUserId(null);

        assertThrows(InvalidTaskException.class,
                () -> taskService.add(dto));
    }

    @Test
    void add_shouldThrowWhenTaskExists() {
        TaskDTO dto = createTestTaskDTO();
        Task task = createTestTask();

        when(taskMapper.toEntity(dto)).thenReturn(task);
        when(repository.findByTitleAndUserId(TASK_TITLE, USER_ID))
                .thenReturn(Optional.of(task));

        assertThrows(DuplicateTaskException.class,
                () -> taskService.add(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void add_shouldSaveNewTask() {
        TaskDTO dto = createTestTaskDTO();
        Task task = createTestTask();
        Task savedTask = createTestTask();

        when(taskMapper.toEntity(dto)).thenReturn(task);
        when(repository.save(task)).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(dto);

        TaskDTO result = taskService.add(dto);

        assertNotNull(result);
        verify(repository).save(task);
    }

    @Test
    void update_shouldThrowWhenIdIsNull() {
        TaskDTO dto = createTestTaskDTO();
        dto.setId(null);

        assertThrows(InvalidTaskException.class,
                () -> taskService.update(dto));
    }

    @Test
    void update_shouldThrowWhenTaskNotFound() {
        TaskDTO dto = createTestTaskDTO();

        when(repository.findById(TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.update(dto));
    }

    @Test
    void update_shouldSendStatusUpdateEventWhenStatusChanged() {
        TaskDTO dto = createTestTaskDTO();
        dto.setStatus("IN_PROGRESS");

        Task existingTask = createTestTask();
        existingTask.setStatus("NEW");

        Task savedTask = createTestTask();
        savedTask.setStatus("IN_PROGRESS");

        when(repository.findById(TASK_ID)).thenReturn(Optional.of(existingTask));
        when(repository.save(existingTask)).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(dto);

        taskService.update(dto);

       verify(taskProducer).sendTaskStatusUpdate(
                new TaskStatusUpdateDTO(TASK_ID, "IN_PROGRESS")
        );
    }

    @Test
    void update_shouldNotSendEventWhenStatusNotChanged() {
        TaskDTO dto = createTestTaskDTO();
        dto.setStatus("NEW"); // Same status

        Task existingTask = createTestTask();
        existingTask.setStatus("NEW");

        when(repository.findById(TASK_ID)).thenReturn(Optional.of(existingTask));
        when(repository.save(existingTask)).thenReturn(existingTask);

        taskService.update(dto);

        verify(taskProducer, never()).sendTaskStatusUpdate(any());
    }

    @Test
    void get_shouldThrowWhenNotFound() {
        when(repository.findById(TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.get(TASK_ID));
    }

    @Test
    void get_shouldReturnTaskDto() {
        Task task = createTestTask();
        TaskDTO dto = createTestTaskDTO();

        when(repository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(dto);

        TaskDTO result = taskService.get(TASK_ID);

        assertEquals(dto, result);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(repository.findById(TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.delete(TASK_ID));
    }

    @Test
    void delete_shouldRemoveTask() {
        Task task = createTestTask();

        when(repository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(createTestTaskDTO());

        TaskDTO result = taskService.delete(TASK_ID);

        assertNotNull(result);
        verify(repository).delete(task);
    }

    @Test
    void getAll_shouldReturnAllTasks() {
        List<Task> tasks = List.of(createTestTask());
        TaskDTO dto = createTestTaskDTO();

        when(repository.findAll()).thenReturn(tasks);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(dto);

        Collection<TaskDTO> result = taskService.getAll();

        assertEquals(1, result.size());
        verify(taskMapper, times(tasks.size())).toDTO(any());
    }
}
