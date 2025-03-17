package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingLogging;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.DuplicateTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.InvalidTaskException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.exception.TaskNotFoundException;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka.TaskProducer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final TaskMapper taskMapper;

    @HandlingLogging
    @HandlingTracking
    public TaskDTO add(TaskDTO taskDTO) {
        if (taskDTO == null) {
            throw new InvalidTaskException("Task DTO cannot be null");
        }
        if (taskDTO.getTitle() == null || taskDTO.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title cannot be empty");
        }
        if (taskDTO.getUserId() == null) {
            throw new InvalidTaskException("User ID cannot be null");
        }

        Task task = taskMapper.toEntity(taskDTO);
        Optional<Task> existingTask = repository.findByTitleAndUserId(task.getTitle(), task.getUserId());

        if (existingTask.isPresent()) {
            log.warn("Task already exists: {}", existingTask.get());
            throw new DuplicateTaskException("Task with this title and user already exists");
        }

        try {
            Task savedTask = repository.save(task);
            return taskMapper.toDTO(savedTask);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTaskException("Task already exists");
        }
    }

    public TaskDTO update(TaskDTO taskDTO) {
        log.info("Updating task: {}", taskDTO);

        if (taskDTO.getId() == null) {
            throw new InvalidTaskException("Task ID cannot be null");
        }

        Task existingTask = repository.findById(taskDTO.getId())
                .orElseThrow(() -> new TaskNotFoundException(taskDTO.getId()));

        String originalStatus = existingTask.getStatus();
        taskMapper.updateTaskFromDTO(taskDTO, existingTask);
        Task updatedTask = repository.save(existingTask);
        TaskDTO updatedTaskDTO = taskMapper.toDTO(updatedTask);
        taskProducer.sendTaskUpdate(updatedTaskDTO);

        if (!originalStatus.equals(updatedTask.getStatus())) {
            taskProducer.sendTaskStatusUpdate(
                    new TaskStatusUpdateDTO(updatedTask.getId(), updatedTask.getStatus())
            );
        }

        return updatedTaskDTO;
    }


    public TaskDTO get(Long id) {
        log.info("Getting task by ID: {}", id);
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toDTO(task);
    }


    public TaskDTO delete(Long id) {
        log.info("Deleting task by ID: {}", id);
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.delete(task);
        return taskMapper.toDTO(task);
    }


    public Collection<TaskDTO> getAll() {
        log.info("Getting all tasks");
        return repository.findAll().stream()
                .map(taskMapper::toDTO)
                .toList();
    }
}