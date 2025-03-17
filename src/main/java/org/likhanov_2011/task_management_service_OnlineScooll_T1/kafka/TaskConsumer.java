package org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.TaskService;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @HandlingTracking
    @KafkaListener(
            topics = "${spring.kafka.topics.task-updates}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "taskDTOKafkaListenerContainerFactory"
    )
    public void handleTaskUpdate(
            TaskDTO taskDTO,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        try {
            taskService.add(taskDTO);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка обработки сообщения: {}", e.getMessage());
        }
    }
}