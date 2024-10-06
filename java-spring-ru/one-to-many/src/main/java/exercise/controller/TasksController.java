package exercise.controller;

import java.util.List;
import java.util.Optional;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.model.Task;
import exercise.model.User;
import exercise.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(t -> taskMapper.map(t)).toList();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        Optional<Task> optTask = taskRepository.findById(id);
        if (optTask.isEmpty()) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        return taskMapper.map(optTask.get());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private TaskDTO createTask(@RequestBody @Valid TaskCreateDTO task) {
        Task crateTask = taskMapper.map(task);
        Optional<User> userOpt = userRepository.findById(task.getAssigneeId());
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Assignee not found: " + task.getAssigneeId());
        }
        crateTask.setAssignee(userOpt.get());
        return taskMapper.map(taskRepository.save(crateTask));
    }

    @PutMapping("/{id}")
    private TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskMapper.update(updateDTO, task);
        Optional<User> userOpt = userRepository.findById(updateDTO.getAssigneeId());
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Assignee not found: " + updateDTO.getAssigneeId());
        }
        task.setAssignee(userOpt.get());
        return taskMapper.map(taskRepository.save(task));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteTask(@PathVariable Long id) {
        taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.deleteById(id);
    }

    // END
}
