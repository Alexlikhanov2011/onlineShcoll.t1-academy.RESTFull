package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.ExceptionHandling;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingLogging;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.InvalidTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.TaskNotFoundException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka.TaskProducer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.stereotype.Service;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final TaskProducer taskProducer;

    @HandlingLogging
    @HandlingTracking
    public Task add(Task task) {
        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title cannot be empty");
        }
        if (task.getId() != null && repository.existsById(task.getId())) {
            return repository.save(task);
        }
        Optional<Task> existingTask = repository.findByTitleAndUserId(task.getTitle(), task.getUserId());
        if (existingTask.isPresent()) {
            log.warn("Задача уже существует: {}", existingTask.get());
            return existingTask.get();
        }
        return repository.save(task);
    }

    public Task get(Long id) {
        log.info("Получение задачи по ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
    @ExceptionHandling
    public Task update(Task task) {
        log.info("Обновление задачи: {}", task);
        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title cannot be empty");
        }

        Task updatedTask = repository.save(task);
        TaskStatusUpdateDTO update = new TaskStatusUpdateDTO(updatedTask.getId(), updatedTask.getStatus());
        taskProducer.sendTaskStatusUpdate(update);
        return updatedTask;
    }


    public Task delete(Long id) {
        log.info("Удаление задачи по ID: {}", id);
        Task entity = repository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task not found"));
        repository.delete(entity);
        return entity;
    }

    public Collection<Task> getAll() {
        log.info("Получение всех задач");
        return repository.findAll();
    }
}