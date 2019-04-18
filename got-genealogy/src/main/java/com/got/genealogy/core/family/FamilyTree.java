package com.got.genealogy.core.family;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;
import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.property.Weight;

import java.util.List;

import static com.got.genealogy.core.family.Direction.DOWN;
import static com.got.genealogy.core.family.Direction.NONE;
import static com.got.genealogy.core.family.Direction.UP;
import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;
import static com.got.genealogy.core.family.person.Relationship.MARRIED;
import static com.got.genealogy.core.family.person.Relationship.PARENT;


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

    public Relation getRelation(String name1, String name2) {
        return getEdge(name1, name2);
    }

    public Relation getRelation(Person person1, Person person2) {
        return getEdge(person1, person2);
    }

    public void addRelation(String name1, String name2, Relation relation) {
        Person person1 = getVertex(name1);
        Person person2 = getVertex(name2);
        // TODO: Prevent acyclic
        // TODO: Prevent invalid, i.e. two people
        // are parents of each other
        if (!(person1 == null) && !(person2 == null)) {
            // TODO: Need some sort of Object -> Relation
            // processor, e.g. String -> Relation
            addRelation(person1, person2, relation);
        }
    }

    public void addRelation(Person person1, Person person2, Relation relation) {
        addEdge(person1, person2, new Weight<>(relation));
    }


    /**
     * 3D coordinate calculator, based on the shortest
     * path between two vertices.
     *
     * @param person1
     * @param person2
     * @return
     */
    public int[] calculateRelationCoords(Person person1, Person person2) {
        List<Person> path = getShortestUnweightedPath(person1, person2, (weights, index) -> {
            boolean married = weights.get(index)
                    .getWeight()
                    .getLabel()
                    .equals(MARRIED.toString());
            boolean parent = weights.get(index)
                    .getWeight()
                    .getLabel()
                    .equals(PARENT.toString());
            return married || parent;
        });
        // Check for empty path
        if (path.size() <= 0) {
            return null;
        }
        Direction previousDirection = NONE;
        Person previousPerson = path.get(0);
        boolean step = false;
        int inLaw = 0;

        // x
        int height = 0;

        // y
        int generation = 0;
        int minGeneration = 0;
        int maxGeneration = 0;

        // z
        int stepCount = 0;

        // Debugging print
        path.forEach((e) -> System.out.print(e.getLabel() + " -> "));

        for (int i = 1; i < path.size(); i++) {
            Relation edge = getEdge(previousPerson, path.get(i));
            if (edge == null) {
                Relation edgeFlipped = getEdge(path.get(i), previousPerson);
                if (edgeFlipped.getLabel().equals(MARRIED.toString())) {
                    inLaw = 1;
                    stepCount++;
                    step = true;
                } else if (edgeFlipped.getLabel().equals(PARENT.toString())){
                    if (previousDirection.equals(DOWN)) {
                        // Change in direction. DOWN -> UP:
                        // Share child, but aren't related.
                        stepCount++;
                        step = true;
                    }
                    // Direction: up
                    // Increment y-axis.
                    generation++;
                    maxGeneration = getMaxInt(generation, maxGeneration);
                    previousDirection = UP;
                }
            } else {
                if (edge.getLabel().equals(MARRIED.toString())) {
                    stepCount++;
                    step = true;
                } else if (edge.getLabel().equals(PARENT.toString())) {
                    // Direction: down
                    if (previousDirection.equals(UP) && (step)) {
                        // Change in direction. UP -> DOWN:
                        // Share parent, but when step relation
                        // has been detected between person1
                        // and the current person, increment
                        // stepCount (z-axis).
                        stepCount++;
                    }
                    // Decrement y-axis.
                    generation--;
                    minGeneration = getMinInt(generation, minGeneration);
                    previousDirection = DOWN;
                }
            }
            // Calculate height, based on the
            // range of generations traversed
            // so far (x-axis).
            height = maxGeneration - minGeneration;
            previousPerson = path.get(i);
        }

        return new int[]{height, generation, stepCount, inLaw};
    }

    /**
     * Shortcut, determining the difference
     * between two integers and returning
     * the smaller value.
     *
     * @param newInt    New integer.
     * @param oldInt    Old integer.
     * @return          Return the smaller
     *                  integer value.
     */
    private int getMinInt(int newInt, int oldInt) {
        if (oldInt < newInt) {
            return oldInt;
        }
        return newInt;
    }

    /**
     * Shortcut, determining the difference
     * between two integers and returning
     * the larger value.
     *
     * @param newInt    New integer.
     * @param oldInt    Old integer.
     * @return          Return the larger
     *                  integer value.
     */
    private int getMaxInt(int newInt, int oldInt) {
        if (oldInt > newInt) {
            return oldInt;
        }
        return newInt;
    }
}

enum Direction {
    UP,
    DOWN,
    NONE
}
