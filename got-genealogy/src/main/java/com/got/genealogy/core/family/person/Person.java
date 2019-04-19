package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Vertex;

import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;

public class Person extends Vertex {
    private Gender gender = UNSPECIFIED;
    private boolean alive = true;

    public Person(String label) {
        super(label); // name
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