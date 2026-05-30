package com.github.trifinyuk.task_management_api.dto;

import com.github.trifinyuk.task_management_api.model.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        OffsetDateTime createdAt
) {
}