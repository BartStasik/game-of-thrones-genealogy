package com.got.genealogy.core.family;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.property.Weight;

import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;


public class FamilyTree extends Graph<Person, Relation> {

    public FamilyTree() {
        super(true);
    }

    public Person getPerson(String name) {
        return getVertex(name);
    }

    public void addPerson(String name) {
        addPerson(name, UNSPECIFIED);
    }

    public void addPerson(String name, Gender gender) {
        // Assumed dead or do we use another enum/int?
        addPerson(name, gender, false);
    }

    public void addPerson(String name, Gender gender, Boolean alive) {
        Person person = new Person(name);
        person.setGender(gender);
        person.setAlive(alive);
        addPerson(person);
    }

    public void addPerson(Person person) {
        addVertex(person);
    }

    public Relation getRelation(Person person1, Person person2) {
        return getEdge(person1, person2);
    }

    public void addRelation(String name1, String name2, Relation relation) {
        Person person1 = getVertex(name1);
        Person person2 = getVertex(name2);

        if (!(person1 == null) && !(person2 == null)) {
            // TODO: Need some sort of Object -> Relation
            // processor, e.g. String -> Relation
            addRelation(person1, person2, relation);
        }
    }

    public void addRelation(Person person1, Person person2, Relation relation) {
        addEdge(person1, person2, new Weight<>(relation));
    }


}
