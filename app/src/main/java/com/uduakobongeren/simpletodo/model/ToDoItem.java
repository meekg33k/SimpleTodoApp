package com.uduakobongeren.simpletodo.model;

/**
 *
 * POJO representing a ToDo Item
 *
 * @author Uduak Obong-Eren
 * @since 8/13/17.
 */

public class ToDoItem {

    private long id;
    private String description;
    private String priority;
    private int isCompleted;
    private String completionDate;

    public ToDoItem(){

    }

    public ToDoItem(String desc, String priority, String date){
        this.description = desc;
        this.priority = priority;
        this.completionDate = date;
        this.isCompleted = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int isCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
