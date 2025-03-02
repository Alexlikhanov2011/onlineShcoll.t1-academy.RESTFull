package org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProducer {

    private final KafkaTemplate<String, TaskDTO> taskDTOKafkaTemplate;
    private final KafkaTemplate<String, TaskStatusUpdateDTO> taskStatusUpdateKafkaTemplate;

    public void sendTaskUpdate(TaskDTO taskDTO) {
        log.info("Отправка задачи в Kafka: {}", taskDTO);
        taskDTOKafkaTemplate.send("task-updates", taskDTO);
    }

    public void sendTaskStatusUpdate(TaskStatusUpdateDTO update) {
        log.info("Отправка обновления статуса задачи в Kafka: {}", update);
        taskStatusUpdateKafkaTemplate.send("task-status-updates", update);
    }
}


