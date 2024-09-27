package com.example.org2;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {


    private Long id;

    private String name;
    private String date;
    private String time;
    private String description;
    private boolean notification;
    private String eventDate;

    private Date outDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
    public  void setEventDate(String eventDate){
        this.eventDate =  eventDate;
    }
    public  String  getEventDate(){
        return  eventDate;
    }
    public void setOutDate(Date outDate){
        this.outDate = outDate;
    }
    public Date getOutDate(){
        return outDate;
    }
}

