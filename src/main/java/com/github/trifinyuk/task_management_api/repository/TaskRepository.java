package com.github.trifinyuk.task_management_api.repository;

import com.github.trifinyuk.task_management_api.model.entity.Task;
import com.github.trifinyuk.task_management_api.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
