package com.got.genealogy.core.family;

import com.got.genealogy.core.graph.Graph;

public class FamilyTree extends Graph<Person, Relation> {

    public FamilyTree(boolean d) {
        super(d);
    }
}
