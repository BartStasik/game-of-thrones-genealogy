package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Vertex;

public class Person extends Vertex {
    private Gender gender;
    private boolean alive;

    public Person(String label) {
        super(label); // name
    }

    public String getName() {
        return getLabel();
    }

    public void setName(String name) {
        setLabel(name);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}