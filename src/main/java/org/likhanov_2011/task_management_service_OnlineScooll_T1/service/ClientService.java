package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ObjectMapper mapper;


    public List<TaskDTO> parseJson() {
        TaskDTO[] clientDtos;
        try {
            clientDtos = mapper.readValue(new File("src/main/resources/DATA.json"), TaskDTO[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(clientDtos);
    }
}
