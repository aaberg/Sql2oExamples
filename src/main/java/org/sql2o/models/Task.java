package org.sql2o.models;

import org.joda.time.DateTime;

/**
* Created by lars on 12/18/13.
*/
public class Task {

    private int id;
    private DateTime dueDate;
    private String description;
    private int priority;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
