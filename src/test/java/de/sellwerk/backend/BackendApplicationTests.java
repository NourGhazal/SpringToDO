package de.sellwerk.backend;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import de.sellwerk.backend.helper.UpdateStatus;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.sellwerk.backend.persistence.TodoEntity;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    final HttpEntity<Void> httpEntity = new HttpEntity<>(new HttpHeaders());

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/todos");
    }

    /**
     * This test creates a new TODO and checks that the TODO is created in the H2 database
     */
    @Test
    void addNewTodo_shouldCreateANewTodo() {
        TodoEntity item = new TodoEntity();
        item.setTitle("TODO 1");
        restTemplate.postForObject(baseUrl, item, TodoEntity.class);

        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl,
                                                                                 HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(1, todos.size());
    }

    /**
     * This test checks that the get endpoint returns all TODO's in default(descending order of created time) sorting order
     */
    @Test
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (1,'TODO 1', 'NEW', {ts '2012-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (2,'TODO 2', 'DONE', {ts '2013-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (3,'TODO 3', 'NEW', {ts '2014-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllTodos_shouldReturnAllTodosInDefaultSortingOrder() {
        final TodoEntity expectedTodo1 = new TodoEntity(3L, "TODO 3", "NEW", LocalDateTime.of(2014, 9, 17, 18, 47, 1));
        final TodoEntity expectedTodo2 = new TodoEntity(2L, "TODO 2", "DONE", LocalDateTime.of(2013, 9, 17, 18, 47, 1));
        final TodoEntity expectedTodo3 = new TodoEntity(1L, "TODO 1", "NEW", LocalDateTime.of(2012, 9, 17, 18, 47, 1));

        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl,
                                                                                 HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});


        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(3, todos.size());

        assertEquals(expectedTodo1, todos.get(0));
        assertEquals(expectedTodo2, todos.get(1));
        assertEquals(expectedTodo3, todos.get(2));
    }

    /**
     * This test checks that the get endpoint returns all TODO's in ascending order of created time when query param sortBy=asc is used
     */
    @Test
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (1,'TODO 1', 'NEW', {ts '2012-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (2,'TODO 2', 'DONE', {ts '2013-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (3,'TODO 3', 'NEW', {ts '2014-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllTodos_whenSortingOrderIsASC_shouldReturnAllTodosInAscendingOrderOfCreatedTime() {
        final TodoEntity expectedTodo1 = new TodoEntity(1L, "TODO 1", "NEW", LocalDateTime.of(2012, 9, 17, 18, 47, 1));
        final TodoEntity expectedTodo2 = new TodoEntity(2L, "TODO 2", "DONE", LocalDateTime.of(2013, 9, 17, 18, 47, 1));
        final TodoEntity expectedTodo3 = new TodoEntity(3L, "TODO 3", "NEW", LocalDateTime.of(2014, 9, 17, 18, 47, 1));


        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl + "?sortBy=asc",
                                                                                 HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});


        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(3, todos.size());

        assertEquals(expectedTodo1, todos.get(0));
        assertEquals(expectedTodo2, todos.get(1));
        assertEquals(expectedTodo3, todos.get(2));
    }


    /**
     * This test checks that the get endpoint returns all TODO's with desired title(should match any todo where title starts with search term) when queryparam ?title={} is used
     */
    @Test
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (1,'TODO 1', 'NEW', {ts '2012-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (2,'TODO 2', 'DONE', {ts '2013-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (3,'TODO 3', 'NEW', {ts '2014-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getTodosByTitle_shouldReturnTodosOfDesiredNameInDefaultSortingOrder() {
        final TodoEntity expectedTodo = new TodoEntity(1L, "TODO 1", "NEW", LocalDateTime.of(2012, 9, 17, 18, 47, 1));

        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl + "?title=TODO 1",
                                                                                 HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});

        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(1, todos.size());

        assertEquals(expectedTodo, todos.get(0));
    }

    @Test
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (1,'TODO 1', 'NEW', {ts '2012-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (2,'TODO 2', 'DONE', {ts '2013-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteToDo_ShouldRemoveTodoFromDataBase() {
        final TodoEntity expectedTodo2 = new TodoEntity(2L, "TODO 2", "DONE", LocalDateTime.of(2013, 9, 17, 18, 47, 1));

        restTemplate.delete(baseUrl+"/TODO 1");

        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl,
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});


        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(1, todos.size());
        assertEquals(expectedTodo2, todos.get(0));
    }

    @Test
    @Sql(statements = "INSERT INTO TODO_ENTITY (id,title,status,created_At) VALUES (1,'TODO 1', 'NEW', {ts '2012-09-17 18:47:01'})", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateToDo_ShouldUpdatedTodoStatusInDatabase() throws IOException {
        final String expectedStatus = "DONE";

        //Alternative way to using restTemplate as it does not support PATCH requests
        CloseableHttpClient httpClient = HttpClients.createDefault();
        var httpPatch = new HttpPatch(baseUrl+"/TODO%201");
        httpPatch.setEntity(new StringEntity("{\"status\": \"DONE\"}", ContentType.APPLICATION_JSON));
        httpClient.execute(httpPatch);

        final ResponseEntity<List<TodoEntity >> response = restTemplate.exchange(baseUrl,
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});


        var todos = response.getBody();
        assertNotNull(todos);
        assertEquals(1, todos.size());
        assertEquals(expectedStatus, todos.get(0).getStatus());
    }
}