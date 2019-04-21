package com.got.genealogy.core.processor.data;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.*;
import static com.got.genealogy.core.family.person.Relationship.*;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;

public class InformationPool {

    public final static Map<Relationship, String> MALE_RELATIONSHIPS;
    public final static Map<Relationship, String> FEMALE_RELATIONSHIPS;

    public final static Relationship[] PARENT_COORDS = new Relationship[]{
            PARENT,
            GRANDPARENT,
            GREAT_GRANDPARENT
    };
    public final static Relationship[] AUNT_UNCLE_COORDS = new Relationship[]{
            AUNT_OR_UNCLE,
            GRANDAUNT_OR_UNCLE,
            GREAT_GRANDAUNT_OR_UNCLE
    };
    public final static Relationship[] CHILD_COORDS = new Relationship[]{
            CHILD,
            GRANDCHILD,
            GREAT_GRANDCHILD
    };
    public final static Relationship[] SIBLING_COORDS = new Relationship[]{
            SIBLING,
            COUSIN
    };
    public final static Relationship[] NIECE_NEPHEW_COORDS = new Relationship[]{
            NIECE_OR_NEPHEW,
            GRANDNIECE_OR_NEPHEW,
            GREAT_GRANDNIECE_OR_NEPHEW
    };

    public final static String[] MALE_INPUTS = new String[]{
            "Man",
            "Male",
            "Husband",
            "Father",
            "Son"
    };
    public final static String[] FEMALE_INPUTS = new String[]{
            "Woman",
            "Female",
            "Wife",
            "Mother",
            "Daughter",
            "Married"
    };

    static {
        Map<Relationship, String> maleRelationships = new HashMap<>();
        Map<Relationship, String> femaleRelationships = new HashMap<>();

        maleRelationships.put(ASCENDANT_COUSIN, "Ascendant Cousin");
        maleRelationships.put(AUNT_OR_UNCLE, "Uncle");
        maleRelationships.put(CHILD, "Son");
        maleRelationships.put(COUSIN, "Cousin");
        maleRelationships.put(DESCENDANT_COUSIN, "Descendant Cousin");
        maleRelationships.put(GRANDAUNT_OR_UNCLE, "Granduncle");
        maleRelationships.put(GRANDCHILD, "Grandson");
        femaleRelationships.put(GRANDNIECE_OR_NEPHEW, "Grandnephew");
        maleRelationships.put(GRANDPARENT, "Grandfather");
        maleRelationships.put(GREAT_GRANDAUNT_OR_UNCLE, "Great Granduncle");
        maleRelationships.put(GREAT_GRANDCHILD, "Great Grandson");
        maleRelationships.put(GREAT_GRANDNIECE_OR_NEPHEW, "Great Grandnephew");
        maleRelationships.put(GREAT_GRANDPARENT, "Great Grandfather");
        maleRelationships.put(HALF_SIBLING, "Half-brother");
        maleRelationships.put(NIECE_OR_NEPHEW, "Nephew");
        maleRelationships.put(PARENT, "Father");
        maleRelationships.put(SIBLING, "Brother");
        maleRelationships.put(SPOUSE, "Husband");
        maleRelationships.put(STEP_CHILD, "Step-Son");
        maleRelationships.put(STEP_PARENT, "Step-Father");

        femaleRelationships.put(AUNT_OR_UNCLE, "Aunt");
        femaleRelationships.put(CHILD, "Daughter");
        femaleRelationships.put(COUSIN, "Cousin");
        femaleRelationships.put(DESCENDANT_COUSIN, "Descendant Cousin");
        femaleRelationships.put(GRANDAUNT_OR_UNCLE, "Grandaunt");
        femaleRelationships.put(GRANDCHILD, "Granddaughter");
        femaleRelationships.put(GRANDNIECE_OR_NEPHEW, "Grandniece");
        femaleRelationships.put(GRANDPARENT, "Grandmother");
        femaleRelationships.put(GREAT_GRANDAUNT_OR_UNCLE, "Great Grandaunt");
        femaleRelationships.put(GREAT_GRANDCHILD, "Great Granddaughter");
        femaleRelationships.put(GREAT_GRANDNIECE_OR_NEPHEW, "Great Grandniece");
        femaleRelationships.put(GREAT_GRANDPARENT, "Great Grandmother");
        femaleRelationships.put(HALF_SIBLING, "Half-sister");
        femaleRelationships.put(NIECE_OR_NEPHEW, "Niece");
        femaleRelationships.put(PARENT, "Mother");
        femaleRelationships.put(SIBLING, "Sister");
        femaleRelationships.put(SPOUSE, "Wife");
        femaleRelationships.put(STEP_CHILD, "Step-Daughter");
        femaleRelationships.put(STEP_PARENT, "Step-Mother");
        femaleRelationships.put(ASCENDANT_COUSIN, "Ascendant Cousin");

        MALE_RELATIONSHIPS = Collections.unmodifiableMap(maleRelationships);
        FEMALE_RELATIONSHIPS = Collections.unmodifiableMap(femaleRelationships);
    }

    public static String getRelationship(Gender gender, Relationship relationship) {
        switch (gender) {
            case MALE:
                return getMaleRelationship(relationship);
            case FEMALE:
                return getFemaleRelationship(relationship);
            default:
                return toTitleCase(relationship.toString());
        }
    }

    public static String getRelationship(Gender gender, Relation relation) {
        try {
            Relationship relationship = Relationship.valueOf(relation.getLabel());
            return getRelationship(gender, relationship);
        } catch (IllegalArgumentException e) {
            return toTitleCase(relation.getLabel());
        }
    }

    public static Relationship getRelationship(Relation relation) {
        return Relationship.valueOf(relation.getLabel());
    }

    public static Relationship getRelationship(String relationship) {
        switch (relationship.toUpperCase()) {
            case "HUSBAND":
            case "WIFE":
            case "MARRIED":
            case "SPOUSE":
                return SPOUSE;
            case "FATHER":
            case "MOTHER":
            case "PARENT":
                return PARENT;
            case "SON":
            case "DAUGHTER":
            case "CHILD":
                return CHILD;
            default:
                return null;
        }
    }

    public static String getMaleRelationship(Relationship relationship) {
        return MALE_RELATIONSHIPS.get(relationship);
    }

    public static String getFemaleRelationship(Relationship relationship) {
        return FEMALE_RELATIONSHIPS.get(relationship);
    }

    public static Gender getGender(String gender) {
        Gender male = getMaleGender(gender);
        Gender female = getFemaleGender(gender);

        if (male == null && female == null) {
            return UNSPECIFIED;
        } else if (male == null) {
            return female;
        } else if (female == null) {
            return male;
        } else {
            return UNSPECIFIED;
        }
    }

    public static Gender getMaleGender(String gender) {
        for (String male : MALE_INPUTS) {
            String genderUp = gender.toUpperCase();
            String maleUp = male.toUpperCase();
            if (genderUp.equals(maleUp)) {
                return MALE;
            }
        }
        return null;
    }

    public static Gender getFemaleGender(String gender) {
        for (String female : FEMALE_INPUTS) {
            String genderUp = gender.toUpperCase();
            String femaleUp = female.toUpperCase();
            if (genderUp.equals(femaleUp)) {
                return FEMALE;
            }
        }
        return null;
    }
}
