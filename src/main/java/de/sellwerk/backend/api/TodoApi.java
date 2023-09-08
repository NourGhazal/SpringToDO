package de.sellwerk.backend.api;

import de.sellwerk.backend.persistence.TodoEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/todos")
public interface TodoApi {

    @PostMapping(value = "", produces = { "application/json" })
    ResponseEntity<TodoEntity> createTodo(@RequestBody TodoEntity todo);

    @GetMapping(value = "",
            produces = { "application/json" })
    ResponseEntity<List<TodoEntity>> getSpecificTodo(
            @Param(value = "title")
            String title,
            @Param(value = "sortBy")
            String sortBy
    );

}
