package org.likhanov_2011.task_management_service_OnlineScooll_T1.dto;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateDTO {
    private Long taskId;
    private String newStatus;

}
