package org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TaskUpdateListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "task-updates", groupId = "task-group")
    public void handleTaskUpdate(TaskStatusUpdateDTO event) {
        log.info("Получено обновление задачи: {}", event);
        notificationService.sendNotification(event);
    }
}
