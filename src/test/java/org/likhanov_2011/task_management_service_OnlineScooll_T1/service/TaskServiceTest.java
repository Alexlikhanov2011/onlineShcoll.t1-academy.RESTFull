package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.InvalidTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.TaskNotFoundException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.repository.TaskRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.List;


class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdd() {
        Task task = new Task(1L, " ", " ", 1L, " ");

        when(repository.save(task)).thenReturn(task);

        Task result = service.add(task);

        assertEquals(task, result);
        verify(repository, times(1)).save(task);
    }

    @Test
    public void testGetFound() {
        long taskId = 1;
        Task task = new Task(1L, " ", " ", 1L, " ");
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = service.get(taskId);

        assertEquals(task, result);
        verify(repository, times(1)).findById(taskId);
    }

    @Test
    public void testGetNotFound() {
        long taskId = 1;
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.get(taskId));
        verify(repository, times(1)).findById(taskId);
    }

    @Test
    public void testUpdateSuccess() {
        Task task = new Task(1L, "ф", "ф", 1L, " ");
        when(repository.save(task)).thenReturn(task);

        Task result = service.update(task);

        assertEquals(task, result);
        verify(repository, times(1)).save(task);
    }

    @Test
    public void testUpdateInvalidTask() {
        Task invalidTask = new Task();

        assertThrows(InvalidTaskException.class, () -> service.update(invalidTask));
    }

    @Test
    public void testDeleteSuccess() {
        long taskId = 1;
        Task task = new Task(1L, " ", " ", 1L, " ");
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = service.delete(taskId);

        assertEquals(task, result);
        verify(repository, times(1)).delete(task);
    }

    @Test
    public void testDeleteNotFound() {
        long taskId = 1;
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.delete(taskId));
        verify(repository, times(1)).findById(taskId);
    }

    @Test
    public void testGetAll() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, " ", " ", 1L, " "));
        taskList.add(new Task(1L, " ", " ", 1L, " "));

        when(repository.findAll()).thenReturn(taskList);

        Collection<Task> results = service.getAll();

        assertEquals(taskList.size(), results.size());
        verify(repository, times(1)).findAll();
    }
}