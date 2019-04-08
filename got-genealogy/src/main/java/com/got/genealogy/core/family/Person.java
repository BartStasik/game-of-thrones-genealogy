package com.got.genealogy.core.family;

import com.got.genealogy.core.graph.property.Vertex;

public class Person extends Vertex {
    private String name;
    private String gender;
    private boolean alive;

    public Person(String label) {
        super(label);
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
