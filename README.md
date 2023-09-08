# backend-assessment

## Project Setup
* Open the project as a maven project in an IDE of your choice.
* Download project dependencies using ```mvn dependency:copy-dependencies```
* Compile project and run all tests using ```mvn install```
* The project should successfully compile but the tests should fail. Goal of the assessment is to ensure that the tests run successfully.

## Project Description
* The overall goal of the project is to implement a basic TODO app with CRUD functionality. 
* The spring boot application currently has no endpoints implemented.
* Integration tests exists for the endpoints GET api/todos and POST api/todos.
* A TODO in our project has an **id**, **title**, **status**(either **NEW** or **DONE**) and **createdTime**.
* The spring boot app should provide endpoints to create new TODO's, get all TODO's and also filter for specific TODO's based on title.
* Implement the POST method *api/todos* with a body to persist new TODO's.
* Implement the GET method *api/todos?title={}&sortBy=asc* to get TODO's.

## Task Description
* The goal of the assessment is make sure all integration tests are passing.
* It's safe to assume that all request data is valid and the implementation of validation is not required for this task.
* The endpoints GET api/todos and POST api/todos should be implemented. Feel free to use any architectural pattern. The application currently has a dependency to H2 database in pom. But any database as desired can be used.
* The endpoint GET api/todos should accept the following query params.
  * title -> use this parameter to filter for TODOS as per desired title. Title should match if the starting letters match. For example, if user searches for ***api/todos?title=clean***, we should match both **clean the room** and **clean the house** TODOS.
  * sortBy -> sorts TODOS based on createdTime. Default is descending order. Possible values: "asc" or "desc"
 
## Nice to haves
* Implement endpoints to change status of a TODO and deleting a TODO.
* Add tests for the new endpoints.

