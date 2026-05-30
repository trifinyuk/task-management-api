package com.github.trifinyuk.task_management_api.dto;

import com.github.trifinyuk.task_management_api.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 4000, message = "Description must not exceed 4000 characters")
        String description,

        @NotNull(message = "Status is required")
        TaskStatus status
) {
}
