package org.likhanov_2011.task_management_service_OnlineScooll_T1.controller;

import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.web.bind.annotation.*;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.TaskService;

import java.util.Collection;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public Task post(@RequestBody Task task) {
        return service.add(task);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Task put(@RequestBody Task task
    ) {
        return service.update(task);
    }

    @DeleteMapping("/{id}")
    public Task delete(@PathVariable long id) {
        return service.delete(id);
    }

    @GetMapping
    public Collection<Task> getAll() {
        return service.getAll();
    }
}
