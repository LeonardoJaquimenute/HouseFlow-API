package com.br.houseflow.service;

import com.br.houseflow.dto.task.CreateTaskDTO;
import com.br.houseflow.dto.task.TaskResponseDTO;
import com.br.houseflow.dto.task.UpdateTaskDTO;
import com.br.houseflow.entity.task.Task;
import com.br.houseflow.entity.task.TaskStatus;
import com.br.houseflow.entity.user.Role;
import com.br.houseflow.entity.user.User;
import com.br.houseflow.exception.TaskNotFoundException;
import com.br.houseflow.exception.UserNotFoundException;
import com.br.houseflow.exception.UnauthorizedOperationException;
import com.br.houseflow.repository.TaskRepository;
import com.br.houseflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskResponseDTO createTask(CreateTaskDTO dto, User authenticatedUser) {

        requireParent(authenticatedUser);

        if (dto.term() == null) {
            throw new IllegalArgumentException("Prazo é obrigatório");
        }

        if (dto.term().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Prazo não pode ser no passado");
        }

        User child = userRepository.findById(dto.childId())
                .orElseThrow(UserNotFoundException::new);

        if (child.getRole() != Role.CHILD) {
            throw new UnauthorizedOperationException();
        }

        if (child.getParent() == null || !child.getParent().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedOperationException();
        }

        Task task = new Task();
        task.setTitulo(dto.titulo());
        task.setDescription(dto.description());
        task.setTerm(dto.term());
        task.setStatus(TaskStatus.CRIADA);
        task.setAssignedTo(child);
        task.setCreatedBy(authenticatedUser);

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    public List<TaskResponseDTO> findByChild(Long childId, User authenticatedUser) {

        requireParent(authenticatedUser);

        User child = userRepository.findById(childId)
                .orElseThrow(UserNotFoundException::new);

        if (child.getParent() == null ||
                !child.getParent().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedOperationException();
        }

        return taskRepository.findByAssignedToId(childId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TaskResponseDTO> findByParent(Long parentId, User authenticatedUser) {
        requireParent(authenticatedUser);

        if (!authenticatedUser.getId().equals(parentId)) {
            throw new UnauthorizedOperationException();
        }

        return taskRepository.findByCreatedById(parentId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TaskResponseDTO updateStatus(Long taskId, UpdateTaskDTO dto, User authenticatedUser) {

        requireParent(authenticatedUser);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getCreatedBy().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedOperationException();
        }

        task.setStatus(dto.status());
        taskRepository.save(task);

        return toDTO(task);
    }

    public void deleteTask(Long taskId, User authenticatedUser) {

        requireParent(authenticatedUser);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getCreatedBy().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedOperationException();
        }

        taskRepository.delete(task);
    }

    private void requireParent(User user) {
        if (user == null) {
            throw new UnauthorizedOperationException();
        }
        if (user.getRole() != Role.PARENT) {
            throw new UnauthorizedOperationException();
        }
    }

    private TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitulo(),
                task.getDescription(),
                task.getTerm(),
                task.getStatus()
        );
    }
}