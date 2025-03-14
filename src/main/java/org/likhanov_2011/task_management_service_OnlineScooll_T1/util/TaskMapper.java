package org.likhanov_2011.task_management_service_OnlineScooll_T1.util;

import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .userId(task.getUserId())
                .status(task.getStatus())
                .build();
    }


    public Task toEntity(TaskDTO taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .userId(taskDTO.getUserId())
                .status(taskDTO.getStatus())
                .build();
    }

    public void updateTaskFromDTO(TaskDTO dto, Task entity) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getUserId() != null) {
            entity.setUserId(dto.getUserId());
        }
    }
}