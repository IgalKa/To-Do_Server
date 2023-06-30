package com.example.ex3;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static com.example.ex3.DataBase.idGenerator;


@RestController
public class Controller {


    private static Log logging = new Log();
    static int requestCounter = 0;
    private DataBase dataBase = new DataBase();

    @GetMapping("/todo/health")
    @ResponseStatus(HttpStatus.OK)
    public String checkHealth()
    {
        Instant start = Instant.now();
        requestCounter++;
        logging.logRequestInfo(requestCounter,"/todo/health","GET");
        logging.logRequestDebug(requestCounter, start);
        return "OK";
    }

    @PostMapping(value = "/todo", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String,?>> createNewToDo(@RequestBody ToDo toDo)
    {
        Instant start = Instant.now();
        requestCounter++;
        logging.logRequestInfo(requestCounter,"/todo","POST");
        String errorMessage;
        Map<String,String> bodyResponse = new HashMap<>();
        Map<String,Integer> bodyResponseInt = new HashMap<>();


        if(isTimeInThePast(toDo.getDueDate()))
        {
            errorMessage = "Error: Can’t create new TODO that its due date is in the past";
            idGenerator--;
            bodyResponse.put("errorMessage", errorMessage);
            logging.logError(errorMessage);
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(bodyResponse);
        }
        else if(dataBase.ToDos.size() != 0)
        {
            if(dataBase.checkToDosNames(toDo.getTitle())) {
                errorMessage = "Error: TODO with the title [" + toDo.getTitle() + "] already exists in the system";
                idGenerator--;
                bodyResponse.put("errorMessage", errorMessage);
                logging.logError(errorMessage);
                logging.logRequestDebug(requestCounter, start);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(bodyResponse);
            }
        }

        logging.logNewTodoInfo(toDo.getTitle());
        logging.logNewTodoDebug(idGenerator - 1,idGenerator);

        dataBase.ToDos.add(toDo);
        bodyResponseInt.put("result", dataBase.getALLCount());
        logging.logRequestDebug(requestCounter, start);
        return ResponseEntity.status(HttpStatus.OK).body(bodyResponseInt);



    }

    @GetMapping("/todo/size")
    public ResponseEntity<Map<String,Integer>> getToDosCount(@RequestParam(value = "status") String status)
    {
        Instant start = Instant.now();

        boolean valid = true;
        Map<String,Integer> bodyResponse = new HashMap<>();

        int count = 0;
        switch (status) {
            case "ALL":
                count = dataBase.getALLCount();
                break;
            case "PENDING":
                count = dataBase.getCountByStatus("PENDING");
                break;
            case "LATE":
                count = dataBase.getCountByStatus("LATE");
                break;
            case "DONE":
                count = dataBase.getCountByStatus("DONE");
                break;
            default:
                valid = false;
                break;
        }


        if(valid) {
            requestCounter++;
            logging.logRequestInfo(requestCounter,"/todo/size","GET");
            logging.logRequestDebug(requestCounter, start);
            logging.logTodoCount(status,count);
            bodyResponse.put("result", count);
            return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @GetMapping("/todo/content")
    public ResponseEntity<Map<String,List<ToDo>>> getTodosData(
            @RequestParam(value = "status") String status,
            @RequestParam(value = "sortBy",  required = false) String sortBy)
    {
        Instant start = Instant.now();
        String sortingBy;
        Map<String,List<ToDo>> responseBody = new HashMap<>();

        List<ToDo> todos = new ArrayList<>();
        if (!status.equals("ALL") && !status.equals("PENDING") && !status.equals("LATE") && !status.equals("DONE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(sortBy != null) {
            if (!sortBy.equals("ID") && !sortBy.equals("DUE_DATE") && !sortBy.equals("TITLE")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            sortingBy = sortBy;
        }
        else
            sortingBy = "ID";

        requestCounter++;
        logging.logRequestInfo(requestCounter,"/todo/content","GET");


        logging.logGetTodoDataInfo(status,sortingBy);


        if(dataBase.ToDos.size() == 0) {
            responseBody.put("result",todos);
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        else if(!status.equals("ALL"))
        {
            for(ToDo todo: dataBase.ToDos)
            {
                if (todo.getStatus().compareTo(status) == 0)
                        todos.add(todo);
            }
        }
        else {
            for(ToDo todo: dataBase.ToDos) {
                    todos.add(todo);
            }
        }

        if(sortBy == null)
            Collections.sort(todos, new TodoIdComparator());
        else if(sortBy.equals("ID"))
            Collections.sort(todos, new TodoIdComparator());
        else if(sortBy.equals("DUE_DATE"))
            Collections.sort(todos, new TodoDueDateComparator());
        else
            Collections.sort(todos, new TodoTitleComparator());


        responseBody.put("result",todos);
        logging.logGetTodoDataDebug(dataBase.ToDos.size(), todos.size());
        logging.logRequestDebug(requestCounter, start);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);


    }

    @PutMapping("/todo")
    public ResponseEntity<Map<String,String>> updateToDoStatus(@RequestParam(value = "id") int id,
                                               @RequestParam(value = "status") String status)
    {
        Instant start = Instant.now();


        if (!status.equals("PENDING") && !status.equals("LATE") && !status.equals("DONE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        requestCounter++;
        logging.logRequestInfo(requestCounter,"/todo","PUT");
        Map<String,String> responseBody = new HashMap<>();
        boolean found = false;
        logging.logUpdateTodoStatusInfo(id,status);


        for(ToDo todo: dataBase.ToDos)
        {
            if(todo.getId() == id)
            {
                found = true;
                responseBody.put("result",todo.getStatus());
                logging.logUpdateTodoStatusDebug(todo.getId(),todo.getStatus(),status);
                todo.setStatus(status);
                break;
            }
        }

        logging.logRequestDebug(requestCounter, start);
        if(found)
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        else
        {
          logging.logError("Error: no such TODO with id " + id);
          responseBody.put("errorMessage","Error: no such TODO with id " + id);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

    }


    @DeleteMapping("/todo")
    public ResponseEntity<Map<String,Object>> deleteToDo (@RequestParam(value = "id") int id)
    {
        Instant start = Instant.now();
        requestCounter++;
        logging.logRequestInfo(requestCounter,"/todo","DELETE");

        Map<String,Object> responseBody = new HashMap<>();
        boolean found = false;
        int count = 0;


        for(ToDo todo: dataBase.ToDos)
        {
            if(todo.getId() == id)
            {
                found = true;
                dataBase.ToDos.remove(todo);
                break;
            }
        }


        if(found)
        {
            logging.logDeleteTodoInfo(id);
            count = dataBase.getALLCount();
            responseBody.put("result", count);
            logging.logDeleteToDoDebug(id,count);
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        else
        {
            responseBody.put("errorMessage","Error: no such TODO with id " + id);
            logging.logError("Error: no such TODO with id \" + id");
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    @GetMapping("/logs/level")
    public ResponseEntity<String> getLoggerLevel(@RequestParam(value = "logger-name") String name)
    {
        Instant start = Instant.now();

        if(name == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The name of the logger wasn't provided");
        }
        else if(name.equals("request-logger") || name.equals("todo-logger"))
        {
            requestCounter++;
            logging.logRequestInfo(requestCounter,"/logs/level","GET");
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.OK).body(logging.getLoggerLevel(name).toUpperCase());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The name provided doesn't match the name of the loggers (request-logger or todo-logger)");
        }
    }


    @PutMapping("/logs/level")
    public ResponseEntity<String> setLoggerLevel(@RequestParam(value = "logger-name") String name,
                                 @RequestParam(value = "logger-level") String level)
    {
        Instant start = Instant.now();
        if (name == null || level == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The required data wasn't provided");
        }
        if(!name.equals("request-logger") && !name.equals("todo-logger")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The name provided doesn't match the name of the loggers (request-logger or todo-logger)");
        }
        if(!level.equals("DEBUG") && !level.equals("INFO") && !level.equals("ERROR"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The level provided doesn't match the name of the supported levels (ERROR,INFO,DEBUG)");
        }
        else {
            requestCounter++;
            logging.logRequestInfo(requestCounter,"/logs/level","PUT");
            logging.setLoggerLevel(name,level.toUpperCase());
            logging.logRequestDebug(requestCounter, start);
            return ResponseEntity.status(HttpStatus.OK).body(logging.getLoggerLevel(name));
        }
    }



    public boolean isTimeInThePast(long millis)
    {
        Date currDate = new Date();
        long currMillis = currDate.getTime();
        if(currMillis > millis)
            return true;
        else
            return false;
    }

    public static class TodoIdComparator implements Comparator<ToDo> {
        @Override
        public int compare(ToDo o1, ToDo o2) {
            return o1.getId() - o2.getId();
        }
    }

    public static class TodoTitleComparator implements Comparator<ToDo> {
        @Override
        public int compare(ToDo o1, ToDo o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }


    public static class TodoDueDateComparator implements Comparator<ToDo> {
        @Override
        public int compare(ToDo o1, ToDo o2) {
            return Long.compare(o1.getDueDate(), o2.getDueDate());
        }
    }




}
