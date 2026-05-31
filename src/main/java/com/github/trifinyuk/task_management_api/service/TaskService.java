package com.github.trifinyuk.task_management_api.service;

import com.github.trifinyuk.task_management_api.dto.TaskRequestDTO;
import com.github.trifinyuk.task_management_api.dto.TaskResponseDTO;
import com.github.trifinyuk.task_management_api.exception.TaskNotFoundException;
import com.github.trifinyuk.task_management_api.model.entity.Task;
import com.github.trifinyuk.task_management_api.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = taskMapper.toEntity(requestDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateEntity(task, requestDTO);
        Task updateTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(updateTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}