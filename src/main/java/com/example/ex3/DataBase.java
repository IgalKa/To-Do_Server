package com.example.ex3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DataBase {
    static int idGenerator = 0;
    List<ToDo> ToDos= new ArrayList<ToDo>();

    public DataBase()
    {
       this.ToDos = new ArrayList<ToDo>();
    }


    public boolean checkToDosNames(String title)
    {
        boolean exists = false;
        for(ToDo todo: this.ToDos)
        {
            if(todo.getTitle().equals(title))
                exists = true;

        }

        return exists;
    }

    public int getCountByStatus(String status)
    {
        int count = 0;

        for(ToDo todo: this.ToDos)
        {
            if (todo.getStatus().equals(status))
                count++;

        }
        return count;
    }

    public int getALLCount()
    {
       return ToDos.size();
    }

}
