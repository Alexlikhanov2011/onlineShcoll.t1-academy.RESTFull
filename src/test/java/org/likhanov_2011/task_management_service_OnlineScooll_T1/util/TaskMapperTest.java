package org.likhanov_2011.task_management_service_OnlineScooll_T1.util;

import org.junit.jupiter.api.Test;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper mapper = new TaskMapper();

    @Test
    void toDTO_ValidTask_ReturnsCorrectDTO() {
        Task task = new Task(1L, "Title", "Desc", 1L, "DONE");
        TaskDTO dto = mapper.toDTO(task);

        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getTitle(), dto.getTitle());
    }

    @Test
    void updateTaskFromDTO_UpdatesOnlyChangedFields() {
        Task entity = new Task(1L, "Old Title", "Old Desc", 1L, "NEW");
        TaskDTO dto = new TaskDTO(null, "New Title", null, null, "DONE");

        mapper.updateTaskFromDTO(dto, entity);

        assertEquals("New Title", entity.getTitle());
        assertEquals("DONE", entity.getStatus());
        assertNotNull(entity.getDescription());
    }
}