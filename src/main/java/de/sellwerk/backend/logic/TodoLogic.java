package de.sellwerk.backend.logic;

import de.sellwerk.backend.persistence.TodoEntity;
import de.sellwerk.backend.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TodoLogic {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoLogic(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    public TodoEntity createTodo(TodoEntity todo) {
        return todoRepository.save(todo);
    }

    public List<TodoEntity> getTodos(String title , String sortBy){
        if(sortBy == null)sortBy= "desc";
        Sort sortDirection = Sort.by("createdAt");
        sortDirection = sortBy.equals("asc")?
                        sortDirection.ascending() :
                        sortDirection.descending();
        if(title == null) return todoRepository.findAllSorted(sortDirection);
        return todoRepository.findByTitle(title,sortDirection);
    }

    @Transactional
    public void deleteTodo(String title){
        todoRepository.deleteByTitle(title);
    }
    @Transactional
    public void updateTodo(String title, String todoStatus){
            todoRepository.updateByTitle(title,todoStatus);
    }


}
