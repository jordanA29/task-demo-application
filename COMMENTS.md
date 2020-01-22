## What kind of refactoring or changes were made to the code?

# Architecture

    I separated the one block code in a "Web, Service, Repository" architecture to respect the Separation of Concerns principle.

    - Web layer is the entry point of the application (controllers, error handler, filter)
    - Service layer contains the Business Logic
    - Repository layer for data

# Entities

    I decided to create the OneToMany association between the User table and the Task table. That way a task has information about its associated user. And that way a user has a set of task which means a task cannot be associated to two users.

# JPARepository

    I chose in my repositories to extend the interface JpaRepository from Spring Data JPA. It reduces the boilerplate code for write / read operations and automatically generates the queries to the database.

# Exception handling

    I created an Exception called TaskNotFoundException thrown every time we try to find a task (by id) that does not exist.

# Field validation

    We take the advantage of the Hibernate Validator by using the @Valid annotation. It will validate the Request Body against the constraints defined in the entity (@NonNull for example). If it fails SpringBoot will throw a MethodArgumentNotValidException.

# Controller advice

    I use a controller advice to handle rest errors. It is used for the TaskNotFoundException to respond with a 404 Not Found error.
    It also handles the Bad Request errors (MethodArgumentNotValidException) to set the header of the response to Error --> Message.

# Filtering

    To handle the Request Parameters dateAfter, includeCompleted and user,  I created a TaskFilter class which implements the Specification interface and overrides its toPredicate() method. It defines a set of condition (presence of the parameter) and returns the constructed predicate. I use this specication alongside with the JpaSpecificationExecutor interface in the repository to indicate that the retrieved entities have to match it. The generated query will contains the predicate conditions.

# Sorting

    Sorting is also possible in the JpaSpecificationExecutor using a Sort object (direction, column name). I construct this object "manually" with the sortBy parameter.

# Why changes were necessary?

    Most of the refactoring has been done to simplify the code (methods are only doing one thing). The Controller code must be as simple as possible and the use of a filter helps to prevent a nested if - else  depending on the number of request parameter (2^number of parameters).

## Improvements in the code

# Unit tests

    I wrote unit tests but all the code is not covered yet.

# Security

    Authentication has not been implemented. We could use Spring Security to protect the endpoints from unauthenticated users (usually behind a /api route).

# Documentation

    The API would need a documentation and could be generated using a tool like Swagger for example.

# Persistence

    The H2 database is used as an in-memory database which means it is created and deleted every time we run the application. We could either configure H2 or an other database. The abstraction provided by Jpa and Hibernate allows us to choose whatever database we want and use it with the associated jdbc driver and configure it in the .properties file.

## Notes

    I had to modify the Unirest request header (set the content to JSON) for the Task creation in the TestServer (that was not supposed to be changed) in order to take advantage of provided field validation.

    unirest sets header to content type plain/text resulting in 415 error if TaskDTO as Request body
    We would need to map the json to dto manually but we would lose annotations validation and hence do it manually
    TaskDTO dto = new ObjectMapper().readValue(taskJson, TaskDTO.class);
    "Manual" validation on fields
