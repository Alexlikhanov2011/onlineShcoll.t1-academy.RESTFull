package org.likhanov_2011.task_management_service_OnlineScooll_T1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "issue not found")
public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(long id) {
        super("issue not found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
