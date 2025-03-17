package org.likhanov_2011.task_management_service_OnlineScooll_T1.repository;

import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long> {
    @Override
    Optional<Task> findById(Long aLong);

    Optional<Task> findByTitleAndUserId(String title, Long userId);
}