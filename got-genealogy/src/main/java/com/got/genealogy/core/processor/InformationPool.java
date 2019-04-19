package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.*;
import static com.got.genealogy.core.family.person.Relationship.*;
import static com.got.genealogy.core.processor.StringUtils.toSentenceCase;

public class InformationPool {

    public final static Map<Relationship, String> MALE_RELATIONSHIPS;
    public final static Map<Relationship, String> FEMALE_RELATIONSHIPS;

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

        maleRelationships.put(SPOUSE, "Husband");
        maleRelationships.put(PARENT, "Father");
        maleRelationships.put(STEP_PARENT, "Step-Father");
        maleRelationships.put(SIBLING, "Brother");
        maleRelationships.put(CHILD, "Son");
        maleRelationships.put(STEP_CHILD, "Step-Son");
        maleRelationships.put(NIECE_NEPHEW, "Nephew");
        maleRelationships.put(AUNT_UNCLE, "Uncle");
        maleRelationships.put(HALF_SIBLING, "Half-brother");
        maleRelationships.put(GRANDPARENT, "Grandfather");
        maleRelationships.put(COUSIN, "Cousin");
        maleRelationships.put(GRANDCHILD, "Grandson");

        femaleRelationships.put(SPOUSE, "Wife");
        femaleRelationships.put(PARENT, "Mother");
        femaleRelationships.put(STEP_PARENT, "Step-Mother");
        femaleRelationships.put(SIBLING, "Sister");
        femaleRelationships.put(CHILD, "Daughter");
        femaleRelationships.put(STEP_CHILD, "Step-Daughter");
        femaleRelationships.put(NIECE_NEPHEW, "Niece");
        femaleRelationships.put(AUNT_UNCLE, "Aunt");
        femaleRelationships.put(HALF_SIBLING, "Half-sister");
        femaleRelationships.put(GRANDPARENT, "Grandmother");
        femaleRelationships.put(COUSIN, "Cousin");
        femaleRelationships.put(GRANDCHILD, "Granddaughter");

        MALE_RELATIONSHIPS = Collections.unmodifiableMap(maleRelationships);
        FEMALE_RELATIONSHIPS = Collections.unmodifiableMap(femaleRelationships);
    }

    public static String getRelationship(Gender gender, Relation relation) {
        try {
            Relationship relationship = Relationship.valueOf(relation.getLabel());
            switch (gender) {
                case MALE:
                    return getMaleRelationship(relationship);
                case FEMALE:
                    return getFemaleRelationship(relationship);
                default:
                    return toSentenceCase(relationship.toString());
            }
        } catch (IllegalArgumentException e) {
            return relation.getLabel();
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
