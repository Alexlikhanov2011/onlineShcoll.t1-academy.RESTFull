package org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Slf4j
@Component
public class TaskProducer {
    private final String taskUpdatesTopic;
    private final String statusUpdatesTopic;
    private final KafkaTemplate<String, TaskDTO> taskDTOKafkaTemplate;
    private final KafkaTemplate<String, TaskStatusUpdateDTO> taskStatusUpdateKafkaTemplate;

    public TaskProducer(
            @Value("${spring.kafka.topics.task-updates}") String taskUpdatesTopic,
            @Value("${spring.kafka.topics.status-updates}") String statusUpdatesTopic,
            @Qualifier("taskDTOKafkaTemplate") KafkaTemplate<String, TaskDTO> taskDTOKafkaTemplate,
            @Qualifier("taskStatusUpdateKafkaTemplate") KafkaTemplate<String, TaskStatusUpdateDTO> taskStatusUpdateKafkaTemplate
    ) {
        this.taskUpdatesTopic = taskUpdatesTopic;
        this.statusUpdatesTopic = statusUpdatesTopic;
        this.taskDTOKafkaTemplate = taskDTOKafkaTemplate;
        this.taskStatusUpdateKafkaTemplate = taskStatusUpdateKafkaTemplate;
    }

    public void sendTaskUpdate(TaskDTO taskDTO) {
        taskDTOKafkaTemplate.send(taskUpdatesTopic, taskDTO);
    }

    public void sendTaskStatusUpdate(TaskStatusUpdateDTO update) {
        taskStatusUpdateKafkaTemplate.send(statusUpdatesTopic, update);
    }
}

