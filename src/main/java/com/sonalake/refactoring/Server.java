package com.sonalake.refactoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@Transactional
public class Server {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${application.host}")
    private String appRoot;

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    /**
     * Used for testing - should clear down all the database entities
     *
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "clear")
    public void clear() throws IOException {
        findAll(Task.class).forEach(t -> entityManager.remove(t));
        findAll(User.class).forEach(t -> entityManager.remove(t));
    }

    /**
     * Get a list of all users in the form {"id": 12345, "name": "billybob"}
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "users")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(findAll(User.class).stream().sorted(Comparator.comparing(u -> u.name)));
    }

    /**
     * Create a task; expects the form {"user": "billybob", "task": "do this", "due": "yyyy-MM-dd"}
     * <p>
     * The identified user will be created in the DB if it's not already there.
     * <p>
     * All fields are mandatory. The due date cannot be in the past.
     *
     * @param taskJson
     * @return the body will be empty, but there will be a "Location" header that will contain a URL to the new resource
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, path = "tasks")
    public ResponseEntity create(@RequestBody String taskJson) throws IOException {
        TaskDTO dto = new ObjectMapper().readValue(taskJson, TaskDTO.class);

        if (null == dto.due) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", "Due can't be empty").build();
        }
        if (toDate(dto.due).before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", "Due can't be in the past").build();
        }
        if (null == dto.user) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", "User can't be empty").build();
        }
        if (null == dto.task) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", "Task can't be empty").build();
        }

        Task task = new Task();
        task.task = dto.task;
        task.due = dto.due;
        task.userId = getOrCreateUser(dto.user).id;
        entityManager.persist(task);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", appRoot + "/tasks/" + task.id.toString())
                .build();
    }

    /**
     * Get a task by its id - returns {"id": 4324324, "user": "billybob", "task": "do this", "due": "yyyy-MM-dd", "isCompleted" : true}
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.GET, path = "tasks/{id}")
    public ResponseEntity get(@PathVariable Long id) throws JsonProcessingException {
        TaskDTO result = findAll(Task.class).stream()
                .filter(t -> t.id.equals(id))
                .map(this::taskToDTO)
                .findFirst().orElse(null);
        if (null != result) {
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(result));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a task by its id
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "tasks/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws JsonProcessingException {
        Task result = findAll(Task.class).stream()
                .filter(t -> t.id.equals(id))
                .findFirst().orElse(null);
        if (null != result) {
            entityManager.remove(result);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Completes a task - i.e. marks isCompleted = true
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "tasks/{id}/complete")
    public ResponseEntity complete(@PathVariable Long id) throws JsonProcessingException {
        Task result = findAll(Task.class).stream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
        if (null != result) {
            result.isCompleted = true;
            entityManager.persist(result);
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(result));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search for tasks - returns values in a list of  {"id": 4324324, "user": "billybob", "task": "do this", "due": "tyyyy-MM-dd", "isCompleted" : true}
     *
     * @param user      optional - if set then only tasks for this user are returned - if the user doesn't exist return nothing
     * @param dateAfter optional - if set then only tasks _after_ this date are returned
     * @param sortBy    sort by the field, in the form: fieldName:asc , fieldName:desc, fieldName (defaults to ascending) - optional, defaults to id:asc
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, path = "tasks")
    public List<TaskDTO> search(
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String dateAfter,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Boolean includeCompleted
    ) throws IOException {

        return findAll(Task.class)
                .stream()
                .map(this::taskToDTO)
                .filter(t -> null == user || t.user.equals(user))
                .filter(t -> null == dateAfter || toDate(dateAfter).before(toDate(t.due)))
                .filter(t -> Boolean.TRUE.equals(includeCompleted) || !t.isCompleted)
                .sorted((o1, o2) -> sort(sortBy, o1, o2))
                .collect(Collectors.toList());

    }

    private int sort(@RequestParam(required = false) String sortBy, TaskDTO o1, TaskDTO o2) {
        if (null == sortBy) {
            return o1.id.compareTo(o2.id);
        } else {
            String sortField = sortBy.contains(":") ? sortBy.substring(0, sortBy.indexOf(":")) : sortBy;
            boolean isDescending = sortBy.contains(":") && sortBy.substring(sortBy.indexOf(":") + 1).equals("desc");

            Comparable f1 = selectField(o1, sortField);
            Comparable f2 = selectField(o2, sortField);

            // sort by ids ascending if the fields are the same
            if (f1.equals(f2)) {
                return o1.id.compareTo(o2.id);
            }
            if (isDescending) {
                return f2.compareTo(f1);
            } else {
                return f1.compareTo(f2);
            }

        }
    }

    private User getOrCreateUser(String userName) {
        User existing = findAll(User.class)
                .stream()
                .filter(u -> u.name.equals(userName))
                .findFirst()
                .orElse(null);

        if (null == existing) {
            existing = new User();
            existing.name = userName;
            entityManager.persist(existing);
        }
        return existing;
    }

    private User findUserById(Long id) {
        return findAll(User.class).stream().filter(u -> u.id.equals(id)).findFirst().orElse(null);
    }

    private TaskDTO taskToDTO(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.due = t.due;
        dto.id = t.id;
        dto.isCompleted = t.isCompleted;
        dto.task = t.task;
        dto.user = findUserById(t.userId).name;
        return dto;
    }

    private Comparable<?> selectField(TaskDTO task, String field) {
        switch (field) {
            case "user":
                return task.user;
            case "id":
                return task.id;
            case "due":
                return task.due;
            case "task":
                return task.task;
            default:
                throw new IllegalArgumentException("Not a field: " + field);
        }
    }

    private Date toDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Bad date: " + date, e);
        }
    }

    private <T> List<T> findAll(Class<T> type) {
        return entityManager.createQuery("from " + type.getSimpleName(), type).getResultList();
    }
}


