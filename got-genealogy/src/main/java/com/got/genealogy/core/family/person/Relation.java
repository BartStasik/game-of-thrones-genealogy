package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Edge;

import java.util.HashSet;
import java.util.Set;

/**
 * Relation is used the Edge
 * between two people in
 * FamilyTree.
 * <p>
 * The Edge can have multiple
 * weights.
 * <p>
 * Real family relationships
 * are kept under label and any
 * extras are stored in a set.
 */
public class Relation extends Edge {

    private Set<String> extras = new HashSet<>();

    /**
     * Relation constructor, forcing
     * an empty label. Any String
     * parameters are automatically
     * stored in the extras set.
     *
     * @param label is the string form
     *              of the relationship
     *              between two people,
     *              allowing for extras.
     */
    public Relation(String label) {
        super("");
        extras.add(label.toUpperCase());
    }

    /**
     * Relation constructor, storing
     * the string value of a Relationship
     * enum as the edge label.
     *
     * @param label is the enum of an
     *              official family-type
     *              relationship.
     */
    public Relation(Relationship label) {
        super(label.toString());
    }

    /**
     * Returns just one extra, if all
     * of them are not needed.
     *
     * @return the first extra.
     */
    public String getTopExtra() {
        return extras.stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns all extra relationships
     * as a set of String objects.
     *
     * @return a set of extra
     * relationships.
     */
    public Set<String> getExtras() {
        return extras;
    }

    /**
     * Add an extra relationship,
     * disregarding input case,
     * to ignore case-sensitivity.
     *
     * @param extra is added to the
     *              extras set.
     */
    public void addExtra(String extra) {
        extras.add(extra.toUpperCase());
    }
}
