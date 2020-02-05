# Refactoring test

A simple coding test

## What is provided

A _very_ poor implementation of a tasks list where 


- The code is all munged into one place
- The code implemented without using the features available in the libraries being used.


There is, however, a set of `e2e` tests in `TestServer` that
 
 - Start the spring boot server
 - Run tests to:
    - Create some tasks
    - Confirm users are created as expected
    - Get tasks by id, and search by name
    - Mark tasks as done
    
    
## What is expected of the candidate?

The candidate willreview the code and make _any_ changes to the `src/main` classes that 
they see fit to make.

This includes, but is not limited to:

- Including any new libraries that would make things easier
- Changing any of the existing classes
- Changing the `java` implementation of the server REST API (the only proviso here is that the `REST` API must still behave as it currently does).

The candidate is also expected to add a `COMMENTS.md` file to document:

- What kind of changes or refactoring were made to the code?
- Why were these changes required?
- Any more improvements in the code you couldn't do but you think it is important and should be done?

**NOTE:** The `TestServer` test is provided to act as a definition of the REST API - this test **MUST NOT** be changed

## What is the API

The purpose of the API is to manage tasks in a team's task list. The APIs are:

### Create a task

Creates new task for a user, in the _incomplete_ state.
```
POST /tasks
```
Body
``` 
{
  "due": "yyyy-MM-dd",
  "task": "do this",
  "user": "billybob"
}
```  

Response 
```
Header: Location: http:localhost:1234/tasks/1234 // URL to the created task
```


**NOTE:**
 
- `due` is required, must be a string date in the format of `yyyy-MM-dd` and must be in the future.
- `task` is required
- `user` is required, but the user need not already exist - the API will create a new entity if needs be.

### Get a task by id
```
GET/tasks/{id} 
```  

Response 
```
{
    "due": "yyyy-MM-dd",
    "id": 4324324,
    "isCompleted": false,
    "task": "do this",
    "user": "billybob"
}
```


### Delete a task by id
```
DELETE/tasks/{id} 
```  

### Mark a task as completed by id

Update a task to mark it as completed

```
PUT/tasks/{id}/complete
```  
Response 
```
{
    "due": "yyyy-MM-dd",
    "id": 4324324,
    "isCompleted": true,
    "task": "do this",
    "user": "billybob"
}
```

### Search tasks
```
GET/tasks?user=billybob&dateAfter=2017-08-26&sortBy=due:desc&includeCompleted=true 
```  
Response 
```
[
{
    "due": "yyyy-MM-dd",
    "id": 4324324,
    "isCompleted": true,
    "task": "do this",
    "user": "billybob"
},{
      "due": "yyyy-MM-dd",
      "id": 383154,
      "isCompleted": true,
      "task": "then do this",
      "user": "billybob"
  }
]
```

Where all fields are **optional**:

- `user` - limit the search to these users.
- `dateAfter` - limit the search to tasks due _after_ this date.
- `sortBy` - any field in the record, the `:desc` or `:asc` are optional, 
  and if left out will assume `asc`. 
  If this field is left out, then `id:desc` is assumed.
- `includeCompleted` - if `true` then completed tasks are included - otherwise 
  only `incomplete` tasks are returned.   Defaults to `false`


### Get all users that have tasks

Gets the users, sorted by username.

```
GET/users 
```  

Response 
```
[
{
    "id": 4324324,
    "name": "billybob"
},{
    "id": 97217,
    "name": "elsie.thompson"
  }
]
```
