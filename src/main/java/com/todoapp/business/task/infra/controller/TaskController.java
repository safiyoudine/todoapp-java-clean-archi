package com.todoapp.business.task.infra.controller;


import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.controller.request.TaskRequest;
import com.todoapp.business.task.infra.controller.response.TaskDto;
import com.todoapp.business.task.infra.mapper.TaskMapper;
import com.todoapp.business.task.usecases.DeleteTask;
import com.todoapp.business.task.usecases.ListTasksWithCurrentUser;
import com.todoapp.business.task.usecases.SaveTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TaskController {

    @Autowired
    private SaveTask saveTask;

    @Autowired
    private DeleteTask deleteTask;

    @Autowired
    private ListTasksWithCurrentUser listTasksWithCurrentUser;



    @PostMapping("/task")
    public ResponseEntity<?> saveTask(@RequestBody TaskRequest taskRequest) {
        Task task = TaskMapper.toDomain(taskRequest);
        TaskDto createdTask = TaskMapper.toDto(saveTask.execute(task));
        if (createdTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable("taskId") Long taskId, @RequestBody TaskRequest taskRequest) {
        Task task = TaskMapper.toDomain(taskRequest);
        task.setId(taskId);
        TaskDto updateTask = TaskMapper.toDto(saveTask.execute(task));

        if (updateTask == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @GetMapping("/tasks/user")
    public ResponseEntity<?> getTasksForConnectedUser() {
        List<Task> taskByCurrentUser = listTasksWithCurrentUser.execute();
        taskByCurrentUser.stream().map(TaskMapper::toDto).forEach(task -> ResponseEntity.ok().body(task));
        return new ResponseEntity<>(taskByCurrentUser, HttpStatus.OK);
    }


    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
        deleteTask.execute(taskId);
        return ResponseEntity.noContent().build();
    }

/*
    @GetMapping("/tasks/user")
    public ResponseEntity<?> getTasksForConnectedUser() {
        List<TaskDto> taskById = taskService.getTasksForConnectedUser();
        return new ResponseEntity<>(taskById, HttpStatus.OK);
    }


    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<TaskDto> pagedResult = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(pagedResult);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskId(@PathVariable("taskId") Long taskId) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }



 */
}
