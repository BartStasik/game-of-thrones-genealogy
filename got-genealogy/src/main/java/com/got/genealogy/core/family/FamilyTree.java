package com.got.genealogy.core.family;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Weight;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.got.genealogy.core.family.Direction.DOWN;
import static com.got.genealogy.core.family.Direction.NONE;
import static com.got.genealogy.core.family.Direction.UP;
import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;
import static com.got.genealogy.core.family.person.Relationship.SPOUSE;
import static com.got.genealogy.core.family.person.Relationship.PARENT;


public class FamilyTree extends Graph<Person, Relation> {

    public FamilyTree(String label) {
        super(label, true);
    }

    public Person getPerson(String name) {
        return getVertex(name);
    }

    public void addPerson(String name) {
        addPerson(new Person(name));
    }

    public void addPerson(String name, Gender gender) {
        Person person = getVertex(name);
        if (person == null) {
            person = new Person(name);
            addVertex(person);
        }
        if (!gender.equals(UNSPECIFIED) && person.getGender().equals(UNSPECIFIED)) {
            person.setGender(gender);
        }
    }

    public void addPerson(Person person) {
        if (!existingVertex(person)) {
            addVertex(person);
        }
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
        if (!(person1 == null) && !(person2 == null)) {
            addRelation(person1, person2, relation);
        }
    }

    public void addRelation(Person person1, Person person2, Relation relation) {
        if (person1.getLabel().equals(person2.getLabel())) {
            return;
        }
        if (getRelation(person1, person2) == null) {
            if (relation.getLabel().equals(SPOUSE.toString())) {
                addEdge(person2, person1, new Weight<>(relation));
            }
            addEdge(person1, person2, new Weight<>(relation));
        }
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
        Person personItem1 = getPerson(person1.getLabel());
        Person personItem2 = getPerson(person2.getLabel());
        if (personItem1 == null || personItem2 == null) {
            return null;
        }
        List<Person> path = getShortestUnweightedPath(personItem1, personItem2, filterPathRelation());
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
            // Todo: change to getRelation
            Relation edge = getEdge(previousPerson, path.get(i), filterRelation());
            if (edge == null) {
                Relation edgeFlipped = getEdge(path.get(i), previousPerson, filterRelation());
                if (edgeFlipped.getLabel().equals(SPOUSE.toString())) {
                    inLaw = 1;
                    stepCount++;
                    step = true;
                } else if (edgeFlipped.getLabel().equals(PARENT.toString())) {
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
                if (edge.getLabel().equals(SPOUSE.toString())) {
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

    private BiFunction<List<Weight<Relation>>, Integer, Boolean> filterPathRelation() {
        return (weights, index) -> {
            boolean married = weights.get(index)
                    .getWeight()
                    .getLabel()
                    .equals(SPOUSE.toString());
            boolean parent = weights.get(index)
                    .getWeight()
                    .getLabel()
                    .equals(PARENT.toString());
            return married || parent;
        };
    }

    private Function<Relation, Boolean> filterRelation() {
        return e -> e.getLabel().equals(SPOUSE.toString()) || e.getLabel().equals(PARENT.toString());
    }

    /**
     * Shortcut, determining the difference
     * between two integers and returning
     * the smaller value.
     *
     * @param newInt New integer.
     * @param oldInt Old integer.
     * @return Return the smaller
     * integer value.
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
     * @param newInt New integer.
     * @param oldInt Old integer.
     * @return Return the larger
     * integer value.
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
