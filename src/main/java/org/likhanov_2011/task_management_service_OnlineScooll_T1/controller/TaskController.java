package org.likhanov_2011.task_management_service_OnlineScooll_T1.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka.TaskProducer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.ClientService;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.TaskService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskProducer taskProducer;
    private final ClientService clientService;
    private final JavaMailSender mailSender;

    @GetMapping("/test-email")
    public String testEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("likhanov-2018@yandex.ru");
            helper.setSubject("Тестовое письмо");
            helper.setText("<p>Это тест из Spring Boot!</p>", true);
            mailSender.send(message);
            return "Письмо отправлено! Проверьте почту (и спам).";
        } catch (MessagingException e) {
            log.error("Ошибка отправки: {}", e.getMessage());
            return "Ошибка: " + e.getCause().getMessage();
        }
    }

    @GetMapping("/parse")
    public void parseSourse() {
        List<TaskDTO> taskDTOS = clientService.parseJson();
        taskDTOS.forEach(taskProducer::sendTaskUpdate);
    }

    @PostMapping
    public TaskDTO post(@RequestBody TaskDTO taskDTO) {
        log.info("Создание задачи: {}", taskDTO);
        Task task = TaskMapper.toEntity(taskDTO);
        Task savedTask = service.add(task);
        return TaskMapper.toDTO(savedTask);
    }

    @GetMapping("/{id}")
    public TaskDTO get(@PathVariable long id) {
        log.info("Получение задачи по ID: {}", id);
        Task task = service.get(id);
        return TaskMapper.toDTO(task);
    }

    @PutMapping("/{id}")
    public TaskDTO put(@RequestBody TaskDTO taskDTO) {
        log.info("Обновление задачи: {}", taskDTO);
        Task task = TaskMapper.toEntity(taskDTO);
        Task updatedTask = service.update(task);
        return TaskMapper.toDTO(updatedTask);
    }

    @DeleteMapping("/{id}")
    public TaskDTO delete(@PathVariable long id) {
        log.info("Удаление задачи по ID: {}", id);
        Task deletedTask = service.delete(id);
        return TaskMapper.toDTO(deletedTask);
    }

    @GetMapping
    public Collection<TaskDTO> getAll() {
        log.info("Получение всех задач");
        return service.getAll().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }
}
