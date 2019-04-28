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

/**
 * InformationPool, used to hold pools of enums
 * and Strings, to extract Gender and Relationship
 * enums, or non-enum relationship strings.
 */
public class InformationPool {

    /**
     * x_RELATIONSHIP Maps, used to pair Relationship
     * enums to their gender-equivalent String outputs.
     */
    public final static Map<Relationship, String> MALE_RELATIONSHIPS;
    public final static Map<Relationship, String> FEMALE_RELATIONSHIPS;

    /**
     * x_COORDS Relationship arrays, used by relationship
     * equations, to find the relationship between two
     * people, to an infinite degree.
     */
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

    /**
     * x_INPUTS String arrays, used to specify the valid
     * input relationship Strings, for which a Gender can
     * be extracted.
     */
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
        maleRelationships.put(GRANDNIECE_OR_NEPHEW, "Grandnephew");
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

    /**
     * Returns the male output String, correlating to
     * the provided Relationship enum.
     *
     * @param relationship Relationship enum.
     * @return the male output String, correlating to
     * the provided Relationship enum.
     */
    public static String getMaleRelationship(Relationship relationship) {
        return MALE_RELATIONSHIPS.get(relationship);
    }

    /**
     * Returns the female output String, correlating to
     * the provided Relationship enum.
     *
     * @param relationship Relationship enum.
     * @return the female output String, correlating to
     * the provided Relationship enum.
     */
    public static String getFemaleRelationship(Relationship relationship) {
        return FEMALE_RELATIONSHIPS.get(relationship);
    }

    /**
     * Returns the relationship between two people in
     * String format.
     * <p>
     * If the Relation stores a valid Relationship enum,
     * a Gender infusion is attempted, otherwise the
     * Relation label is returned in Title Case.
     *
     * @param gender   of the Person.
     * @param relation between two Person objects.
     * @return the relationship between two people in
     * String format.
     */
    public static String getRelationship(Gender gender, Relation relation) {
        try {
            Relationship relationship = Relationship.valueOf(relation.getLabel());
            return getRelationship(gender, relationship);
        } catch (IllegalArgumentException e) {
            return toTitleCase(relation.getLabel());
        }
    }

    /**
     * Returns the Relationship enum as a String, if
     * a corresponding output String exists. This is
     * where the Gender gets infused.
     *
     * @param gender       of the Person.
     * @param relationship Relationship enum.
     * @return the Relationship enum as a String, if
     * * a corresponding output String exists. This is
     * * where the Gender gets infused.
     */
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

    /**
     * Extracts either SPOUSE, PARENT or CHILD
     * Relationship enums, if the provided input String
     * is valid.
     *
     * @param relationship input relationship String.
     * @return either SPOUSE, PARENT or CHILD
     * Relationship enums, if the provided input String
     * is valid.
     */
    public static Relationship getFilteredInputRelationship(String relationship) {
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

    /**
     * Extracts the Gender enum from an input String.
     *
     * @param gender input gender String.
     * @return UNSPECIFIED if a valid Gender could
     * not be extracted.
     */
    public static Gender getInputGender(String gender) {
        Gender male = getMaleInputGender(gender);
        Gender female = getFemaleInputGender(gender);
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

    /**
     * Returns null if a valid male input String
     * could not be found in MALE_INPUTS.
     *
     * @param gender input male String.
     * @return null if a valid male input String
     * could not be found in MALE_INPUTS.
     */
    public static Gender getMaleInputGender(String gender) {
        for (String male : MALE_INPUTS) {
            String genderUp = gender.toUpperCase();
            String maleUp = male.toUpperCase();
            if (genderUp.equals(maleUp)) {
                return MALE;
            }
        }
        return null;
    }

    /**
     * Returns null if a valid male input String
     * could not be found in FEMALE_INPUTS.
     *
     * @param gender input female String.
     * @return null if a valid male input String
     * could not be found in FEMALE_INPUTS.
     */
    public static Gender getFemaleInputGender(String gender) {
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
