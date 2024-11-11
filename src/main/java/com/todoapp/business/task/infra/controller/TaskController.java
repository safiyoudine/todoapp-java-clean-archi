package com.todoapp.business.task.infra.controller;


import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.controller.request.TaskRequest;
import com.todoapp.business.task.infra.controller.request.UpdateTaskRequest;
import com.todoapp.business.task.infra.controller.response.TaskDto;
import com.todoapp.business.task.infra.mapper.TaskMapper;
import com.todoapp.business.task.usecases.DeleteTask;
import com.todoapp.business.task.usecases.FindTaskWithCurrentUser;
import com.todoapp.business.task.usecases.ListTasksWithCurrentUser;
import com.todoapp.business.task.usecases.SaveTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TaskController {

    @Autowired
    private SaveTask saveTask;

    @Autowired
    private FindTaskWithCurrentUser findTaskWithCurrentUser;

    @Autowired
    private DeleteTask deleteTask;

    @Autowired
    private ListTasksWithCurrentUser listTasksWithCurrentUser;

    @PostMapping("/task")
    public ResponseEntity<?> saveTask(@RequestBody TaskRequest request) {
        Task task = TaskMapper.toDomain(request);
        TaskDto createdTask = TaskMapper.toDto(saveTask.execute(task));
        if (createdTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable("taskId") Long taskId, @RequestBody UpdateTaskRequest request) {
        Task task = TaskMapper.toDomain(request);
        task.setId(taskId);
        TaskDto updateTask = TaskMapper.toDto(saveTask.execute(task));

        if (updateTask == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Task> taskByCurrentUser = listTasksWithCurrentUser.execute(page, size);
        Page<TaskDto> taskDtos = taskByCurrentUser.map(TaskMapper::toDto);
        return new ResponseEntity<>(taskDtos, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskId(@PathVariable("taskId") Long taskId) {
        Task task = findTaskWithCurrentUser.execute(taskId);
        TaskDto taskDto = TaskMapper.toDto(task);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }


    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
        deleteTask.execute(taskId);
        return ResponseEntity.noContent().build();
    }
}
