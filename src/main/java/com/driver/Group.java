package com.driver;

import java.util.List;

public class Group {
    private String name;
    private int numberOfParticipants;


    public Group(String name, List<User> users) {
        this.name = name;
        this.numberOfParticipants = users.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}