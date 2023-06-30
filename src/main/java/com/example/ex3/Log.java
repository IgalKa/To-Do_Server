package com.example.ex3;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;

import java.time.Duration;
import java.time.Instant;

import static com.example.ex3.Controller.requestCounter;


public class Log {

    private static final Logger requestLogger = LogManager.getLogger("request-logger");
    private static final Logger todoLogger = LogManager.getLogger("todo-logger");


    public void logRequestInfo(int requestNum,String resource, String HTTPVerb)
    {
        ThreadContext.put("requestNumber", String.valueOf(requestCounter));
        requestLogger.info("Incoming request | #{} | resource: {} | HTTP Verb {}",requestNum,resource,HTTPVerb);
    }

    public void logRequestDebug(int requestNum, Instant start)
    {
        Instant end = Instant.now();
        long duration=  Duration.between(start, end).toNanos();
        ThreadContext.put("requestNumber", String.valueOf(requestCounter));
        requestLogger.debug("request #{} duration: {}ms",requestNum,duration);
    }

    public void logNewTodoInfo(String title)
    {
        todoLogger.info("Creating new TODO with Title [{}]",title);
    }
    public void logNewTodoDebug(int count,int id)
    {
        todoLogger.debug("Currently there are {} TODOs in the system." +
                " New TODO will be assigned with id {}",count,id);
    }
    public void logTodoCount(String state,int count)
    {
        todoLogger.info("Total TODOs count for state {} is {}",state,count);
    }

    public void logGetTodoDataInfo(String filter,String sortID)
    {
        todoLogger.info("Extracting todos content. Filter: {} | Sorting by: {}", filter, sortID);
    }

    public void logGetTodoDataDebug(int count, int resCount)
    {
        todoLogger.debug("There are a total of {} todos in the system." +
                " The result holds {} todos",count,resCount);
    }

    public void logUpdateTodoStatusInfo(int id, String state)
    {
        todoLogger.info("Update TODO id [{}] state to {}",id,state);
    }

    public void logUpdateTodoStatusDebug(int id,String oldState,String newState)
    {
        todoLogger.debug("Todo id [{}] state change: {} --> {}",id,oldState,newState);
    }

    public void logDeleteTodoInfo(int id)
    {
        todoLogger.info("Removing todo id {}",id);
    }

    public void logDeleteToDoDebug(int id,int count)
    {
        todoLogger.debug("After removing todo id [{}] there are {} TODOs in the system",id,count);
    }

    public void logError(String msg)
    {
        todoLogger.error("{}",msg);
    }

    public String getLoggerLevel(String name)
    {
        if(name.equals("request-logger"))
            return requestLogger.getLevel().toString();
        else
            return todoLogger.getLevel().toString();
    }

    public void setLoggerLevel(String name,String level)
    {
        if(level.equals("INFO"))
             Configurator.setLevel(name,Level.INFO);
        else if(level.equals("DEBUG"))
            Configurator.setLevel(name,Level.DEBUG);
        else
            Configurator.setLevel(name,Level.ERROR);
    }





}


