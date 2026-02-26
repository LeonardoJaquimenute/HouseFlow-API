package com.br.houseflow.controller;

import com.br.houseflow.dto.task.CreateTaskDTO;
import com.br.houseflow.dto.task.TaskResponseDTO;
import com.br.houseflow.dto.task.UpdateTaskDTO;
import com.br.houseflow.security.CustomUserDetails;
import com.br.houseflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@PreAuthorize("hasRole('PARENT')")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private static com.br.houseflow.entity.user.User currentUser(CustomUserDetails userDetails) {
        return userDetails.user();
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody CreateTaskDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = currentUser(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(dto, user));
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<TaskResponseDTO>> findByChild(
            @PathVariable Long childId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = currentUser(userDetails);
        return ResponseEntity.ok(taskService.findByChild(childId, user));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponseDTO>> myTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = userDetails.user();
        return ResponseEntity.ok(taskService.findByParent(user.getId(), user));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = currentUser(userDetails);
        return ResponseEntity.ok(taskService.updateStatus(taskId, dto, user));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = currentUser(userDetails);
        taskService.deleteTask(taskId, user);
        return ResponseEntity.noContent().build();
    }
}