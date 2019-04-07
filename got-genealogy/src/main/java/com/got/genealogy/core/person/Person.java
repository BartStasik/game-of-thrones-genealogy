package com.got.genealogy.core.person;

import com.got.genealogy.core.graph.Vertex;

public class Person extends Vertex {
    private String name;
    private String gender;
    private boolean alive;

    public Person(String label) {
        super(label);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
