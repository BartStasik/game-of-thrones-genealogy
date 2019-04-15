package com.got.genealogy;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Relation;

import java.util.HashMap;

import static com.got.genealogy.core.family.person.Gender.FEMALE;
import static com.got.genealogy.core.family.person.Gender.MALE;
import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;

public class TestFamilyTree {

    public static void main(String[] args) {
        // Should eventually be in the
        // file processor or controller file?
        FamilyTree family = new FamilyTree();

        HashMap<String, Gender> names = new HashMap<>();

        // Based purely on example input
        // file, mentioned in the cw spec.
        names.put("Eddard Stark", MALE);
        names.put("Arya Stark", FEMALE);
        names.put("Robb Stark", UNSPECIFIED);
        names.put("Sansa Stark", UNSPECIFIED);
        names.put("Bran Stark", UNSPECIFIED);
        names.put("Rickon Stark", UNSPECIFIED);
        names.put("Lyanna Stark", FEMALE);
        names.put("Rickard Stark", MALE);
        names.put("Catelyn Tully", FEMALE);
        names.put("Jon Snow", UNSPECIFIED);
        names.put("Rhaegar Targaryen", MALE);

        // Add person with name and gender
        names.forEach(family::addPerson);

        // Both mother/father will be generic
        // and gender will be injected when
        // getting.
        // Eddard Stark, father
        family.addRelation("Eddard Stark",
                "Arya Stark",
                new Relation("father"));
        family.addRelation("Eddard Stark",
                "Robb Stark",
                new Relation("father"));
        family.addRelation("Eddard Stark",
                "Sansa Stark",
                new Relation("father"));
        family.addRelation("Eddard Stark",
                "Bran Stark",
                new Relation("father"));
        family.addRelation("Eddard Stark",
                "Rickon Stark",
                new Relation("father"));

        // Catelyn Tully, mother
        family.addRelation("Catelyn Tully",
                "Arya Stark",
                new Relation("mother"));
        family.addRelation("Catelyn Tully",
                "Robb Stark",
                new Relation("mother"));
        family.addRelation("Catelyn Tully",
                "Sansa Stark",
                new Relation("mother"));
        family.addRelation("Catelyn Tully",
                "Bran Stark",
                new Relation("mother"));
        family.addRelation("Catelyn Tully",
                "Rickon Stark",
                new Relation("mother"));

        // Rickard Stark, father
        family.addRelation("Rickard Stark",
                "Eddard Stark",
                new Relation("father"));
        family.addRelation("Rickard Stark",
                "Lyanna Stark",
                new Relation("father"));

        // Lyanna Stark, mother
        family.addRelation("Lyanna Stark",
                "Jon Snow",
                new Relation("mother"));

        // Rhaegar Targaryen, father
        family.addRelation("Rhaegar Targaryen",
                "Jon Snow",
                new Relation("father"));

        // Both mother and father are
        // 6 characters long.
        family.printGraph(new Relation("______"));
    }
}
