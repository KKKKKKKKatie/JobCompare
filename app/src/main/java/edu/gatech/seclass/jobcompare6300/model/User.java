package edu.gatech.seclass.jobcompare6300.model;

public class User {
    private int userId = -1;// Can be null if no current job

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

