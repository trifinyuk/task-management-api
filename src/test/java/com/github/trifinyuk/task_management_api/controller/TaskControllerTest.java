package com.github.trifinyuk.task_management_api.controller;

import com.github.trifinyuk.task_management_api.dto.TaskRequestDTO;
import com.github.trifinyuk.task_management_api.dto.TaskResponseDTO;
import com.github.trifinyuk.task_management_api.exception.TaskNotFoundException;
import com.github.trifinyuk.task_management_api.model.enums.TaskStatus;
import com.github.trifinyuk.task_management_api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void createTask_ShouldReturnCreated_WhenValidRequest() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO("New Task", "Description", TaskStatus.PENDING);
        TaskResponseDTO response = new TaskResponseDTO(1L, "New Task", "Description", TaskStatus.PENDING, OffsetDateTime.now());

        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        TaskRequestDTO invalidRequest = new TaskRequestDTO("", "", null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        TaskResponseDTO task1 = new TaskResponseDTO(1L, "Task1", "Desc1", TaskStatus.PENDING, OffsetDateTime.now());
        TaskResponseDTO task2 = new TaskResponseDTO(2L, "Task2", "Desc2", TaskStatus.COMPLETED, OffsetDateTime.now());

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Task1")))
                .andExpect(jsonPath("$[1].status", is("COMPLETED")));
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() throws Exception {
        TaskResponseDTO task = new TaskResponseDTO(1L, "Pending Task", "Desc", TaskStatus.PENDING, OffsetDateTime.now());

        when(taskService.getTasksByStatus(TaskStatus.PENDING)).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].status", is("PENDING"))));

    }

    @Test
    void getTaskById_ShouldReturnTask_WhenExists() throws Exception {
        TaskResponseDTO task = new TaskResponseDTO(1L, "Task", "Desc", TaskStatus.PENDING, OffsetDateTime.now());

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task")));
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenValidRequest() throws Exception {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Updated", "New Desc", TaskStatus.COMPLETED);
        TaskResponseDTO updatedResponse = new TaskResponseDTO(1L, "Updated", "New Desc", TaskStatus.COMPLETED, OffsetDateTime.now());

        when(taskService.updateTask(eq(1L), any(TaskRequestDTO.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated")))
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        TaskRequestDTO updateRequest = new TaskRequestDTO("Updated", "New Desc", TaskStatus.COMPLETED);

        when(taskService.updateTask(eq(99L), any(TaskRequestDTO.class)))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/api/tasks/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskExists() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        doThrow(new TaskNotFoundException(99L))
                .when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/{id}", 99))
                .andExpect(status().isNotFound());
    }
}