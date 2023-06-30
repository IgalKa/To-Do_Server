
# To-Do Server

A server side of a To-Do application.


The server allows users to maintain a list of things they need to do (i.e. a To-Do).   
Users can create a To-Do, update when it is fulfilled, manage priorities between all To-Do’s, etc.

This project was written in Java using Spring Boot.

# Dependencies
* Make sure you have Java 17 installed, if not you can download it from here: https://www.oracle.com/il-en/java/technologies/downloads/#java17
* log4j2
* Spring-boot ver 3.0.5

# Getting Started


Clone the project

```bash
  git clone https://github.com/IgalKa/To-Do_Server
```

Navigate to the project's directory and open the project using an IDE like Intellij, build the project and run.

   



# How To Use

The server listens on port 9285, example for an HTTP request: http://localhost:9285/todo/size?status=ALL

Each To-Do has the below properties:

* Id: a unique id which is a simple integer counter that starts from 1  
* Title: short title describing what’s the essence of this To-Do  
* Content: the actual content describing what this To-Do stands for  
* Due date: a timestamp denoting the target time for this To-Do to be fulfilled  
* Status: the status of the To-Do: PENDING (when it is created and before the due date) | LATE (when it is not performed yet, and we are past the due date) | DONE (when the To-Do  item processing is over)

The server enables to add, update & delete To-Do’s by sending HTTP requests to the server

The supported endpoints are:


* **Create new To-Do**  
  Creates a new To-Do item in the system.   
  Endpoint: /todo  
  Method: POST  
  Body: json object:  
  {  
  title: <To-Do title>   
  content: <To-Do content>   
  dueDate: <timestamp in millis>   
  }  

* **Get To-Dos count**  
  Returns the total number of To-Dos in the system, according to the given filter.  
  Endpoint: /todo/size  
  Method: GET  
  Query Parameter: status. Value: ALL, PENDING, LATE, DONE (in capital case only).  


* **Get To-Dos data**  
  Returns the content of the To-Dos according to the supplied status  
  Endpoint: /todo/content  
  Method: GET  
  Query Parameter: status. Value: ALL, PENDING, LATE, DONE   
  Query Parameter: sortBy. Value: ID, DUE_DATE, TITLE  (This is an optional query    parameter).  


* **Update To-Do status**  
  Updates To-Do status property   
  Endpoint: /todo  
  Method: PUT  
  Query Parameter: id. Number. The To-Do id  
  Query Parameter: status. The status to update. It can be PENDING, LATE, or DONE  


* **Delete To-Do**  
  Deletes a To-Do object.  
  Endpoint: /todo  
  Method: DELETE  
  Query Parameter: id. Number. The To-Do id  

* **Get logger level**  
  Get the current logging level of a logger (DEBUG, ERROR, etc)  
  Endpoint: /logs/level  
  Method: GET  
  Query Parameter: logger-name: the name of the logger (request-logger or todo-logger)  


* **Set logger level**  
  Set the logging level of a logger (DEBUG, ERROR, etc)   
  Endpoint: /logs/level    
  Method: PUT   
  Query Parameter: logger-name: the name of the logger (request-logger or todo-logger)  
  Query Parameter: logger-level: the requested level: ERROR, INFO, DEBUG  

 
