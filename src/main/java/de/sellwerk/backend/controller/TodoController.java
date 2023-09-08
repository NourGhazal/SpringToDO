package de.sellwerk.backend.controller;

import de.sellwerk.backend.api.TodoApi;
import de.sellwerk.backend.logic.TodoLogic;
import de.sellwerk.backend.persistence.TodoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoController implements TodoApi {
    private final TodoLogic todoLogic;

    @Autowired
    public TodoController(TodoLogic todoLogic) {
        this.todoLogic = todoLogic;
    }

    @Override
        public ResponseEntity<TodoEntity> createTodo(TodoEntity todo) {
                var savedTodo =  todoLogic.createTodo(todo);
                return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<List<TodoEntity>> getSpecificTodo(String title, String sortBy) {
        var allTodos = todoLogic.getTodos(title,sortBy);
        return new ResponseEntity<>(allTodos,HttpStatus.OK);
    }
}
