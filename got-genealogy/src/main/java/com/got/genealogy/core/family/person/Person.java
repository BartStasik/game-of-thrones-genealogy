package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Vertex;

import java.util.HashMap;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;

public class Person extends Vertex {
    private Gender gender = UNSPECIFIED;
    private Map<String, String> details = new HashMap<>();

    public Person(String label) {
        super(label); // name
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public String getDetail(String key) {
        return details.get(key.toUpperCase());
    }

    public void setDetail(String key, String value) {
        details.replace(
                key.toUpperCase(),
                value.toUpperCase());
    }

    public void addDetail(String key, String value) {
        details.put(
                key.toUpperCase(),
                value.toUpperCase());
    }

    public void removeDetail(String key) {
        details.remove(key.toUpperCase());
    }
}