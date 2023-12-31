package com.mutipolar.bootcamp.crud.controller;

import com.mutipolar.bootcamp.crud.domain.Todo;
import com.mutipolar.bootcamp.crud.dto.ErrorMessage;
import com.mutipolar.bootcamp.crud.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
//    public Todo createTodo(@Valid @RequestBody Todo todo){
//        return todoService.createOrUpdateTodo(todo);
//    }
    public ResponseEntity<?> createTodo(@Valid @RequestBody Todo todo,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ErrorMessage> validationErrors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setCode("VALIDATION_ERROR");
                errorMessage.setMessage(error.getDefaultMessage());
                validationErrors.add(errorMessage);
            }
            return ResponseEntity.badRequest().body(validationErrors);
        }
        Todo createdTodo = todoService.createOrUpdateTodo(todo);
        return ResponseEntity.ok(createdTodo);
    }

    @GetMapping
    public List<Todo> getAllTodos(){
        return todoService.getAllTodos();
    }

//    get todo by id lewat pathvariable/segment
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id){
        Optional<Todo> todo = todoService.getTodoById((id));
        return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

//    edit todo by id
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo){
        todo.setId(id);
        return todoService.createOrUpdateTodo(todo);
    }

//    delete todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoId(@PathVariable String id){
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{task}")
    public ResponseEntity<Todo> getTodoByTask(@PathVariable String task){
        Optional<Todo> todo = todoService.findByTaskCaseInsensitive(task);
        return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
