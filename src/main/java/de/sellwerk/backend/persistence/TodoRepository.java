package de.sellwerk.backend.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntity, Long> {

    @Query("select x from TodoEntity x where x.title=?1")
    List<TodoEntity> findByTitle(String title, Sort sort);
    @Query("select x from TodoEntity x")
    List<TodoEntity> findAllSorted(Sort sort);
}
