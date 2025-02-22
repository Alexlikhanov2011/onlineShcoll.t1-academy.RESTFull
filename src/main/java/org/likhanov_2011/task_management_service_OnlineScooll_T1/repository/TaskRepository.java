package org.likhanov_2011.task_management_service_OnlineScooll_T1.repository;

import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
}
