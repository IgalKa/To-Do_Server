package com.example.ex3;





import static com.example.ex3.DataBase.idGenerator;


public class ToDo {


    private int id;
    private String title;
    private String content;
    private String status;
    private long dueDate;



    public ToDo(String description, String content, long DueDate) {
        this.id = ++idGenerator;
        this.title = description;
        this.content = content;
        this.status = "PENDING";
        this.dueDate = DueDate;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String description) {
        this.title = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
