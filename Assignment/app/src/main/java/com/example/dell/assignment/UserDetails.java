package com.example.dell.assignment;

public class UserDetails {
    String name, entity, phone;
    int regionOfWork, id;
    boolean available;

    UserDetails()
    {
        available=true;
    }
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRegionOfWork() {
        return regionOfWork;
    }

    public void setRegionOfWork(int regionOfWork) {
        this.regionOfWork = regionOfWork;
    }
}
