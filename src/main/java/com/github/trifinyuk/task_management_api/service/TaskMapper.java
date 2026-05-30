package com.github.trifinyuk.task_management_api.service;

import com.github.trifinyuk.task_management_api.dto.TaskRequestDTO;
import com.github.trifinyuk.task_management_api.dto.TaskResponseDTO;
import com.github.trifinyuk.task_management_api.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }

    public Task toEntity(TaskRequestDTO requestDTO) {
        Task task = new Task();
        task.setTitle(requestDTO.title());
        task.setDescription(requestDTO.description());
        task.setStatus(requestDTO.status());
        return task;
    }

    public void updateEntity(Task task, TaskRequestDTO requestDTO) {
        task.setTitle(requestDTO.title());
        task.setDescription(requestDTO.description());
        task.setStatus(requestDTO.status());
    }
}
