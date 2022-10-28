package com.eduquette.model;

import java.io.Serializable;

/**
 * Class to represent the user of the application
 */
public class User implements Serializable {
    public static String ID = "USER";

    // Instance variable to hold the name
    private String name;

    /**
     * Constructor for the user class
     *
     * @param name the name of the user
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Get the name of the user
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the user
     *
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }
}
