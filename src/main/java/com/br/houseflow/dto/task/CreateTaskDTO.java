package com.br.houseflow.dto.task;

import java.time.LocalDate;

public record CreateTaskDTO(
        String titulo,
        String description,
        LocalDate term,
        Long childId
) {}

