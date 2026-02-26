package com.br.houseflow.dto.task;

import com.br.houseflow.entity.task.TaskStatus;

import java.time.LocalDate;


public record TaskResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDate prazo,
        TaskStatus status
) {}
