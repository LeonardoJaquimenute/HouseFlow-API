package com.br.houseflow.dto.task;

import com.br.houseflow.entity.task.TaskStatus;

public record UpdateTaskDTO(
        TaskStatus status
) {}
