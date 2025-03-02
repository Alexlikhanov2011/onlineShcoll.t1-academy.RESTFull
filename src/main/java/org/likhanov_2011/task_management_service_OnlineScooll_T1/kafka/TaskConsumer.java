package org.likhanov_2011.task_management_service_OnlineScooll_T1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.service.TaskService;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.util.TaskMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final TaskService taskService;

    @HandlingTracking
    @KafkaListener(topics = "task-updates", groupId = "task-group", containerFactory = "taskDTOKafkaListenerContainerFactory")
    public void handleTaskUpdate(TaskDTO taskDTO) {
        try {
            Task task = TaskMapper.toEntity(taskDTO);
            taskService.add(task);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Конфликт версий при сохранении задачи: {}", taskDTO.getId());
        }
    }
}