package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.ExceptionHandling;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingLogging;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingResult;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.InvalidTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.TaskNotFoundException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.stereotype.Service;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.repository.TaskRepository;

import java.util.Collection;


@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
@HandlingResult
    public Task add(Task task) {
        return repository.save(task);
    }
@HandlingLogging
    public Task get(long id) {
        return repository.findById(id).
                orElseThrow(()-> new TaskNotFoundException(id));
    }
@ExceptionHandling
    public Task update(Task task) {
    if (task == null) {
        throw new InvalidTaskException("Task cannot be null");
    }
    if (task.getTitle() == null || task.getTitle().isBlank()) {
        throw new InvalidTaskException("Task title cannot be empty");
    }
    return repository.save(task);
    }

    @HandlingTracking
    public Task delete(long id) {
        Task entity = repository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task not found"));
        repository.delete(entity);
        return entity;
    }
    @HandlingTracking
@HandlingResult
    public Collection<Task> getAll() {
        return repository.findAll();
    }
}
