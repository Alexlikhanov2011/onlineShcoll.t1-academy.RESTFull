package org.likhanov_2011.task_management_service_OnlineScooll_T1.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private String status;
}
