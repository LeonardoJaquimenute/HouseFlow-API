package com.br.houseflow.repository;

import com.br.houseflow.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedToId(Long childId);

    List<Task> findByCreatedById(Long parentId);

    Optional<Task> findByIdAndCreatedById(Long taskId, Long parentId);

    Optional<Task> findByIdAndAssignedToId(Long taskId, Long childId);

}

