package org.likhanov_2011.task_management_service_OnlineScooll_T1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka.TaskProducer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.ClientService;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.springframework.web.bind.annotation.*;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.TaskService;

import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskProducer taskProducer;
    private final ClientService clientService;

    @GetMapping("/parse")
    public void parseSourse() {
        List<TaskDTO> taskDTOS = clientService.parseJson();
        taskDTOS.forEach(taskProducer::sendTaskUpdate);
    }

    @PostMapping
    public TaskDTO post(@RequestBody TaskDTO taskDTO) {
        log.info("Создание задачи: {}", taskDTO);
        return taskService.add(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO get(@PathVariable long id) {
        log.info("Получение задачи по ID: {}", id);
        return taskService.get(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@RequestBody TaskDTO taskDTO) {
        log.info("Обновление задачи: {}", taskDTO);
        return taskService.update(taskDTO);
    }

    @DeleteMapping("/{id}")
    public TaskDTO deleteTask(@PathVariable Long id) {
        log.info("Удаление задачи по ID: {}", id);
        return taskService.delete(id);
    }

    @GetMapping
    public Collection<TaskDTO> getAll() {
        log.info("Получение всех задач");
        return taskService.getAll();
    }
}
