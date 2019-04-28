package com.got.genealogy.core.family;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;
import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.property.Weight;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.got.genealogy.core.family.Direction.DOWN;
import static com.got.genealogy.core.family.Direction.NONE;
import static com.got.genealogy.core.family.Direction.UP;
import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;
import static com.got.genealogy.core.family.person.Relationship.CHILD;
import static com.got.genealogy.core.family.person.Relationship.PARENT;
import static com.got.genealogy.core.family.person.Relationship.SPOUSE;


public class FamilyTree extends Graph<Person, Relation> {

    /**
     * FamilyTree constructor, forcing the specification
     * of a label.
     *
     * @param label of the tree, used to differentiate
     *              between multiple FamilyTree objects.
     */
    public FamilyTree(String label) {
        super(label, true);
    }

    /**
     * Get a Person object, using their label.
     *
     * @param name of the person being pulled.
     * @return the Person object, if existing or null if
     * they don't.
     */
    public Person getPerson(String name) {
        // Regardless of case
        return getVertex(name, String::toUpperCase);
    }

    /**
     * Adds a new vertex in the graph, using a person's
     * name, so long as they don't already exist.
     * <p>
     * The Gender of a new person defaults to UNSPECIFIED.
     *
     * @param name of the new person.
     * @return the new Person object, or one that exists
     * with that name.
     */
    public Person addPerson(String name) {
        Person person = getPerson(name);
        if (person != null) {
            return person;
        }
        return addPerson(new Person(name));
    }

    /**
     * Adds a new vertex in the graph, using a person's
     * name.
     * <p>
     * The Person object is given a Gender, which is
     * only overwritten if the enum is something other
     * than UNSPECIFIED.
     * <p>
     * If the Person object is already stored in the
     * graph, their gender is updated, following the
     * rules above.
     *
     * @param name   of the new/existing person.
     * @param gender of the new/existing person.
     * @return the new or already-existing Person object.
     */
    public Person addPerson(String name, Gender gender) {
        Person person = getPerson(name);
        if (person == null) {
            person = new Person(name);
            addVertex(person);
        }
        if (!gender.equals(UNSPECIFIED) && person.getGender().equals(UNSPECIFIED)) {
            person.setGender(gender);
        }
        return person;
    }

    /**
     * Adds a new vertex in the graph, if they don't
     * already exit.
     *
     * @param person is a Person object to be added as a
     *               vertex in the graph.
     * @return the new or already-existing Person object.
     */
    public Person addPerson(Person person) {
        return addVertex(person);
    }

    /**
     * Returns the edge between people, using their
     * label, only if they and the edge itself, actually
     * exist.
     *
     * @param name1 label of person1.
     * @param name2 label of person2.
     * @return the Relation object if it exists, otherwise
     * return null.
     */
    public Relation getRelation(String name1, String name2) {
        return getEdge(name1, name2);
    }

    /**
     * Returns the edge between people, only if they,
     * and the edge itself, actually exist.
     *
     * @param person1 is a Person object that has to be
     *                an existing vertex in the graph.
     * @param person2 is a Person object that has to be
     *                an existing vertex in the graph.
     * @return the Relation object if it exists,
     * otherwise return null.
     */
    public Relation getRelation(Person person1, Person person2) {
        return getEdge(person1, person2);
    }

    /**
     * Add a new edge between two people, using their
     * names and a valid family-tree Relationship enum.
     * <p>
     * If an edge already exists between them, the
     * label is overwritten, but the extras are kept
     * the same.
     * <p>
     * For a CHILD Relationship enum, an edge is added,
     * in the opposite direction, and with the PARENT
     * Relationship enum.
     * <p>
     * For a SPOUSE Relationship enum, an edge is also
     * added in the opposite direction.
     *
     * @param name1        label of person1.
     * @param name2        label of person2.
     * @param relationship is the Relationship enum,
     *                     allowing for only valid
     *                     values.
     */
    public void addRelation(String name1,
                            String name2,
                            Relationship relationship) {
        Person person1 = getPerson(name1);
        Person person2 = getPerson(name2);

        if (!(person1 == null) && !(person2 == null)) {
            Relation relationFrom1 = editRelation(name1, name2, relationship);
            Relation relationFrom2;
            if (relationship.equals(CHILD)) {
                relationFrom2 = editRelation(name2, name1, PARENT);
                addRelation(person2, person1, relationFrom2);
            } else {
                if (relationship.equals(SPOUSE)) {
                    relationFrom2 = editRelation(name2, name1, SPOUSE);
                    addRelation(person2, person1, relationFrom2);
                }
                addRelation(person1, person2, relationFrom1);
            }
        }
    }

    /**
     * Add a new edge between two people, preventing
     * self-loops.
     * <p>
     * The addEdge() method prevents adding edges if one
     * already exists.
     *
     * @param person1  is a Person object that has to be
     *                 an existing vertex in the graph.
     * @param person2  is a Person object that has to be
     *                 an existing vertex in the graph.
     * @param relation is the new edge between two people,
     *                 later wrapped in the Weight class.
     */
    public void addRelation(Person person1,
                            Person person2,
                            Relation relation) {
        if (person1.getLabel().equals(person2.getLabel())) {
            return;
        }
        addEdge(person1, person2, new Weight<>(relation));
    }

    /**
     * Add a new edge between two people. If it
     * already exists, replace the relationship label,
     * but keep all extras.
     *
     * @param name1        label of person1.
     * @param name2        label of person2.
     * @param relationship is a valid family-tree
     *                     relationship.
     * @return the new or existing Relation object
     * itself.
     */
    public Relation editRelation(String name1,
                                 String name2,
                                 Relationship relationship) {
        Relation relation = getRelation(name1, name2);
        if (relation == null) {
            relation = new Relation(relationship);
        } else if (relation.getLabel() != null) {
            relation.setLabel(relationship.toString());
        }
        return relation;
    }

    /**
     * Add a relationship, directly to the extras map,
     * if the same one doesn't already exist between
     * people.
     *
     * @param name1        label of person1.
     * @param name2        label of person2.
     * @param relationship is the extra relationship.
     */
    public void addExtraRelation(String name1,
                                 String name2,
                                 String relationship) {
        Person person1 = getPerson(name1);
        Person person2 = getPerson(name2);
        Relation relation = getRelation(name1, name2);

        if (!(person1 == null) && !(person2 == null)) {
            if (relation == null) {
                relation = new Relation(relationship);
            } else {
                relation.addExtra(relationship);
            }
            addRelation(person1, person2, relation);
        }
    }

    /**
     * 4D coordinate calculator, based on the shortest
     * path between two vertices.
     *
     * @param person1 Starting person, used to search
     *                how they are related to person2.
     * @param person2 Goal person, to whom person1 is
     *                related to.
     * @return 4 coordinates: tree height, generation
     * difference, non-blood step count and
     * marriages-passed count.
     */
    public int[] calculateRelationCoords(Person person1, Person person2) {
        Person personItem1 = getPerson(person1.getLabel());
        Person personItem2 = getPerson(person2.getLabel());
        if (personItem1 == null || personItem2 == null) {
            return null;
        }
        List<Person> path = getShortestUnweightedPath(
                personItem1,
                personItem2,
                filterPathRelation());
        // Check for empty path
        if (path.size() <= 0) {
            return null;
        }
        Direction previousDirection = NONE;
        Person previousPerson = path.get(0);
        boolean step = false;

        // x
        int height = 0;

        // y
        int generation = 0;
        int minGeneration = 0;
        int maxGeneration = 0;

        // z
        int noBloodCount = 0;

        // p
        int inLawCount = 0;

        for (int i = 1; i < path.size(); i++) {
            Relation edge = getEdge(
                    previousPerson,
                    path.get(i),
                    filterRelation());
            Relation edgeFlipped = getEdge(
                    path.get(i),
                    previousPerson,
                    filterRelation());
            if (edge == null) {
                if (edgeFlipped.getLabel().equals(SPOUSE.toString())) {
                    // Count how many marriages
                    // have been traversed
                    // through.
                    inLawCount++;
                } else if (edgeFlipped.getLabel()
                        .equals(PARENT.toString())) {
                    if (previousDirection.equals(DOWN)) {
                        // Change in direction. DOWN -> UP:
                        // Share child, but aren't related.
                        noBloodCount++;
                        step = true;
                    }
                    // Direction: up
                    // Increment y-axis.
                    generation++;
                    maxGeneration = getMaxInt(generation, maxGeneration);
                    previousDirection = UP;
                }
            } else {
                if (edge.getLabel()
                        .equals(SPOUSE.toString())) {
                    // Count how many marriages
                    // have been traversed
                    // through.
                    inLawCount++;
                } else if (edge.getLabel()
                        .equals(PARENT.toString())) {
                    // Direction: down
                    if (previousDirection.equals(UP) && (step)) {
                        // Change in direction. UP -> DOWN:
                        // Share parent, but when step relation
                        // has been detected between person1
                        // and the current person, increment
                        // stepCount (z-axis).
                        noBloodCount++;
                    }
                    // Decrement y-axis.
                    generation--;
                    minGeneration = getMinInt(generation, minGeneration);
                    previousDirection = DOWN;
                }
            }
            // Calculate tree height, based on
            // the range of generations traversed
            // so far (x-axis).
            height = maxGeneration - minGeneration;
            previousPerson = path.get(i);
        }

        return new int[]{height, generation, noBloodCount, inLawCount};
    }

    /**
     * Filter, returning a BiFunction that only
     * accepts SPOUSE or PARENT Relationship enums.
     *
     * @return a BiFunction to be used as a
     * parameter for all methods, filtering
     * Relation objects, wrapped in the Weight
     * wrapper, stored in a List.
     */
    private BiFunction<List<Weight<Relation>>, Integer, Boolean> filterPathRelation() {
        return (weights, index) -> {
            Relation relation = weights.get(index)
                    .getWeight();
            if (relation == null) {
                return false;
            }
            boolean married = relation.getLabel()
                    .equals(SPOUSE.toString());
            boolean parent = relation.getLabel()
                    .equals(PARENT.toString());
            return married || parent;
        };
    }

    /**
     * Filter, returning a Function that only
     * accepts SPOUSE or PARENT Relationship enums.
     *
     * @return a Function to be used as a parameter
     * for all methods, filtering Relation objects
     * and using a boolean to exclude others.
     */
    private Function<Relation, Boolean> filterRelation() {
        return e -> e.getLabel()
                .equals(SPOUSE.toString()) ||
                e.getLabel().equals(PARENT.toString());
    }

    /**
     * Shortcut, determining the difference between
     * two integers and returning the smaller value.
     *
     * @param newInt New integer.
     * @param oldInt Old integer.
     * @return Return the smaller integer value.
     */
    private int getMinInt(int newInt, int oldInt) {
        if (oldInt < newInt) {
            return oldInt;
        }
        return newInt;
    }

    /**
     * Shortcut, determining the difference between
     * two integers and returning the larger value.
     *
     * @param newInt New integer.
     * @param oldInt Old integer.
     * @return Return the larger integer value.
     */
    private int getMaxInt(int newInt, int oldInt) {
        if (oldInt > newInt) {
            return oldInt;
        }
        return newInt;
    }
}

/**
 * Enum used by calculateRelationCoords.
 * Determines fixed "virtual" tree
 * traversal values.
 */
enum Direction {
    UP,
    DOWN,
    NONE
}
