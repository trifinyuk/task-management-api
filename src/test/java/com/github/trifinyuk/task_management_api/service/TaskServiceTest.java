package com.github.trifinyuk.task_management_api.service;

import com.github.trifinyuk.task_management_api.dto.TaskRequestDTO;
import com.github.trifinyuk.task_management_api.dto.TaskResponseDTO;
import com.github.trifinyuk.task_management_api.exception.TaskNotFoundException;
import com.github.trifinyuk.task_management_api.model.entity.Task;
import com.github.trifinyuk.task_management_api.model.enums.TaskStatus;
import com.github.trifinyuk.task_management_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequestDTO requestDTO;
    private TaskResponseDTO responseDTO;

    @BeforeEach
    void setUP() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(OffsetDateTime.now());

        requestDTO = new TaskRequestDTO("Test Task", "Test Description", TaskStatus.PENDING);

        responseDTO = new TaskResponseDTO(1L, "Test Task", "Test Description", TaskStatus.PENDING, task.getCreatedAt());
    }

    @Test
    void createTask_ShouldReturnResponseDTO_WhenSuccess() {
        when(taskMapper.toEntity(requestDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.createTask(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_ShouldReturnListOfResponseDTO() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredList_WhenTasksExist() {
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(List.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        List<TaskResponseDTO> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().status()).isEqualTo(TaskStatus.PENDING);
        verify(taskRepository, times(1)).findByStatus(TaskStatus.PENDING);
    }

    @Test
    void getTasksByStatus_ShouldReturnEmptyList_WhenNoTasks() {
        when(taskRepository.findByStatus(TaskStatus.COMPLETED)).thenReturn(List.of());

        List<TaskResponseDTO> result = taskService.getTasksByStatus(TaskStatus.COMPLETED);

        assertThat(result).isEmpty();
        verify(taskRepository, times(1)).findByStatus(TaskStatus.COMPLETED);
    }

    @Test
    void getTaskById_ShouldReturnResponseDTO_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateTask_ShouldReturnUpdatedResponseDTO_WhenTaskExists() {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Update Title", "Update Desc", TaskStatus.IN_PROGRESS);
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Update Title");
        updatedTask.setDescription("Update Desc");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setCreatedAt(task.getCreatedAt());

        TaskResponseDTO updatedResponse = new TaskResponseDTO(1L, "Update Title", "Update Desc", TaskStatus.IN_PROGRESS, task.getCreatedAt());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(taskMapper.toResponseDTO(updatedTask)).thenReturn(updatedResponse);

        TaskResponseDTO result = taskService.updateTask(1L, updateRequest);

        assertThat(result.title()).isEqualTo("Update Title");
        assertThat(result.status()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Update Title", "Update Desc", TaskStatus.COMPLETED);

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, updateRequest))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldDelete_WhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }
}