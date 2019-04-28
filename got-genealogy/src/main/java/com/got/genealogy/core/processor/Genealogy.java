package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.got.genealogy.core.family.person.Relationship.*;
import static com.got.genealogy.core.processor.data.FileHandler.*;
import static com.got.genealogy.core.processor.data.InformationPool.*;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;

/**
 * Genealogy class, used to read input text files,
 * to store relationships between people and details
 * on them in a FamilyTree object.
 * <p>
 * Handles outputting to files, containing either a
 * sorted list of people, or the FamilyTree in DOT
 * format.
 * <p>
 * Finds relationships between two people in a
 * FamilyTree.
 * <p>
 * Stores multiple FamilyTree objects and identifies
 * them using their labels (non case-sensitive).
 */
public class Genealogy {

    private static List<FamilyTree> families = new ArrayList<>();

    /**
     * Returns null if there isn't any FamilyTree
     * with that name.
     *
     * @param familyName label of the FamilyTree.
     * @return null if there isn't any FamilyTree
     * with that name.
     */
    public static FamilyTree getFamily(String familyName) {
        for (FamilyTree family : families) {
            if (family.getLabel()
                    .toUpperCase()
                    .equals(familyName.toUpperCase())) {
                return family;
            }
        }
        return null;
    }

    /**
     * Returns the FamilyTree under an index in
     * the List.
     *
     * @param familyIndex index of the FamilyTree
     *                    in the List.
     * @return the FamilyTree under an index in
     * the List.
     */
    public static FamilyTree getFamily(int familyIndex) {
        return families.get(familyIndex);
    }

    /**
     * Replaces the FamilyTree under an index in
     * the List.
     *
     * @param familyIndex index of the FamilyTree
     *                    in the List.
     * @param familyTree  to replace the previous
     *                    FamilyTree.
     *                    FamilyTree.
     */
    public static void setFamily(int familyIndex, FamilyTree familyTree) {
        families.set(familyIndex, familyTree);
    }

    /**
     * Adds new FamilyTree to the List.
     *
     * @param familyTree new FamilyTree.
     */
    public static void addFamily(FamilyTree familyTree) {
        families.add(familyTree);
    }

    /**
     * Removes a FamilyTree, if one exists with
     * the specified name.
     *
     * @param familyName label of the FamilyTree.
     */
    public static void removeFamily(String familyName) {
        FamilyTree family = getFamily(familyName);
        if (family != null) {
            removeFamily(family);
        }
    }

    /**
     * Removes a FamilyTree under an index in the
     * List.
     *
     * @param familyIndex index of the FamilyTree
     *                    in the List.
     */
    public static void removeFamily(int familyIndex) {
        families.remove(familyIndex);
    }

    /**
     * Removes a FamilyTree directly from the List.
     * Removes a FamilyTree directly from the List.
     *
     * @param familyTree FamilyTree to be removed.
     */
    public static void removeFamily(FamilyTree familyTree) {
        families.remove(familyTree);
    }

    /**
     * Returns a Map of detail types and their
     * corresponding detail Strings, for an existing
     * Person in FamilyTree.
     * <p>
     * Gets Person and FamilyTree with their labels
     * and returns null if either one doesn't exist.
     *
     * @param personName label of the Person.
     * @param familyName label of the FamilyTree.
     * @return a Map of detail types and their
     * corresponding detail Strings, for an existing
     * Person in FamilyTree.
     */
    public static Map<String, String> getPersonDetails(String personName,
                                                       String familyName) {
        FamilyTree family = getFamily(familyName);
        Map<String, String> details;
        Person person;
        if (family == null) {
            return null;
        }
        person = family.getPerson(personName);
        if (person == null) {
            return null;
        }
        details = person.getDetails();
        details.put("GENDER", person.getGender().toString());
        return details;
    }

    /**
     * Loads the specified file in resources, for an
     * existing or new FamilyTree, containing details
     * on specific people.
     *
     * @param resourceStream InputStream of file in
     *                       resources.
     * @param familyName     name of an existing or
     *                       new FamilyTree.
     * @return <tt>true</tt> if loading was successful.
     */
    public static boolean loadPersonDetailsFile(InputStream resourceStream,
                                                String familyName) {
        try {
            String[][] file = loadResourceFile(resourceStream);
            FamilyTree family = getFamily(familyName);

            String personName;
            Person person;

            if (file == null || family == null) {
                return false;
            }

            for (String[] row : file) {
                if (row.length < 2 || row.length > 3)
                    return false;

                personName = row[0];
                person = family.getPerson(personName);

                if (person == null)
                    person = family.addPerson(personName);

                if (row[1].toUpperCase().equals("GENDER")) {
                    if (row.length != 3)
                        return false;
                    person.setGender(getInputGender(row[2]));
                } else {
                    switch (row.length) {
                        case 2:
                            person.addDetail(row[1], "Unknown");
                            break;
                        case 3:
                            person.addDetail(row[1], row[2]);
                            break;
                        default:
                            return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads the specified file in resources, for an
     * existing or new FamilyTree, containing
     * relationships between people.
     *
     * @param resourceStream InputStream of file in
     *                       resources.
     * @param familyName     name of an existing or
     *                       new FamilyTree.
     * @return <tt>true</tt> if loading was successful.
     */
    public static FamilyTree loadRelationsFile(InputStream resourceStream,
                                               String familyName) {
        try {
            String[][] file = loadResourceFile(resourceStream);
            FamilyTree family = getFamily(familyName);

            if (file == null) {
                return null;
            }

            if (family == null) {
                family = new FamilyTree(familyName);
                addFamily(family);
            }

            for (String[] row : file) {
                String name1, name2;
                Gender gender;
                Relationship relationship;

                switch (row.length) {
                    case 1:
                        name1 = row[0];
                        family.addPerson(name1);
                        break;
                    case 2:
                        name1 = row[0];
                        gender = getInputGender(row[1]);
                        family.addPerson(name1, gender);
                        break;
                    case 3:
                        name1 = row[0];
                        name2 = row[2];
                        gender = getInputGender(row[1]);
                        relationship = getFilteredInputRelationship(row[1]);

                        if (relationship != null) {
                            family.addPerson(name1, gender);
                            family.addPerson(name2);
                            family.addRelation(name1, name2, relationship);
                        } else {
                            family.addPerson(name1);
                            family.addPerson(name2);
                            family.addExtraRelation(name1, name2, row[1]);
                        }
                        break;
                    default:
                        System.out.println("INCORRECT FILE FORMAT");
                        return null;
                }
            }

            return family;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Exports the FamilyTree in DOT format to the
     * absolute path specified.
     * <p>
     * Returns null if the FamilyTree doesn't exist.
     *
     * @param absolutePath of the destination output
     *                     file.
     * @param familyName   name of an existing or
     *                     new FamilyTree.
     * @return null if the FamilyTree doesn't exist.
     */
    public static String[] exportDOT(String absolutePath,
                                     String familyName) {
        FamilyTree family = getFamily(familyName);
        return family == null ? null : exportGVFile(absolutePath, family);
    }

    /**
     * Exports a list of all people in a FamilyTree,
     * sorted alphabetically, in ascending order.
     * <p>
     * Returns null if the FamilyTree doesn't exist.
     *
     * @param absolutePath of the destination
     *                     output file.
     * @param familyName   name of an existing or
     *                     new FamilyTree.
     * @return null if the FamilyTree doesn't exist.
     */
    public static Person[] exportSorted(String absolutePath,
                                        String familyName) {
        FamilyTree family = getFamily(familyName);
        return family == null ? null : exportSortedFile(absolutePath, family);
    }

    /**
     * Returns a String array of all relationships
     * between two people, including official
     * family-tree relationships and extras, such
     * as "loves", "hates", etc.
     * <p>
     * Returns null if either one of the people or
     * the FamilyTree doesn't exist.
     *
     * @param name1      of the first Person.
     * @param name2      of the second Person.
     * @param familyName of the FamilyTree.
     * @return a String array of all relationships
     * between two people, including official
     * family-tree relationships and extras, such
     * as "loves", "hates", etc.
     */
    public static String[] findRelationship(String name1,
                                            String name2,
                                            String familyName) {
        FamilyTree family = getFamily(familyName);
        if (family == null) {
            return null;
        }
        Person person1 = family.getPerson(name1);
        Person person2 = family.getPerson(name2);
        if (person1 == person2 || person1 == null || person2 == null) {
            return null;
        }
        return processRelationship(person1, person2, family);
    }

    /**
     * Returns a String array of all people in
     * a FamilyTree, sorted alphabetically,
     * in ascending order.
     *
     * @param familyName label of the FamilyTree.
     * @return a String array of all people in
     * a FamilyTree, sorted alphabetically,
     * in ascending order.
     */
    public static String[] getAllPeople(String familyName) {
        if (familyName == null) {
            return null;
        }
        FamilyTree family = getFamily(familyName);
        if (family == null) {
            return null;
        }
        Set<Person> people = family.getVertices().keySet();
        List<Person> sortedPeople = new ArrayList<>(people);
        String[] allPeople = new String[sortedPeople.size()];

        Collections.sort(sortedPeople);
        for (int i = 0; i < sortedPeople.size(); i++) {
            allPeople[i] = sortedPeople.get(i).getLabel();
        }
        return allPeople;
    }

    /**
     * Returns a String array of all relationships
     * between two people, including official
     * family-tree relationships and extras, such as
     * "loves", "hates", etc.
     * <p>
     * Processes 4 coordinates, to determine a family
     * relationship, after finding the shortest path
     * between two people in the DAG. The path forms
     * a tree of PARENT to CHILD or SPOUSE to SPOUSE.
     * <p>
     * The 4 coordinates are:
     * <ul>
     * <li>x = height of the tree</li>
     * <li>y = generational difference of
     * person 2 to person 1</li>
     * <li>z = the number of times the path
     * crossed a child to non-partner
     * relationship, i.e. had a child together
     * but aren't partners</li>
     * <li>p = the number of time the path
     * crossed spousal relationships</li>
     * </ul>
     * <p>
     * When y < 0, x can be replaced with x += y,
     * after which point the x coordinate is now the
     * lowest common ancestor of two people, which
     * can be used to form linear equations for
     * coordinates x and y.
     *
     * @param person1 whom is related to person2.
     * @param person2 whom person1 is related to.
     * @param family  FamilyTree, in which two people
     *                are to be related in.
     * @return a String array of all relationships
     * between two people, including official
     * family-tree relationships and extras, such as
     * "loves", "hates", etc.
     */
    private static String[] processRelationship(Person person1,
                                                Person person2,
                                                FamilyTree family) {
        int x, y, z, p;
        int[] coordinates;
        String relationship;

        List<String> relationships = new ArrayList<>();

        Relation direct = family.getRelation(person1, person2);
        Gender gender = person1.getGender();
        boolean inLaw = false;

        if (direct != null) {
            if (direct.getExtras().size() > 0) {
                // Get all extra relations,
                // but still needs to search
                // if any distant family relation.
                for (String extra : direct.getExtras()) {
                    relationships.add(toTitleCase(extra));
                }
            } else {
                relationship = getRelationship(gender, direct);
                if (!relationship.isEmpty()) {
                    return finalRelationship(relationships, relationship);
                }
            }
        }

        coordinates = family.calculateRelationCoords(person1, person2);

        if (coordinates != null) {
            x = coordinates[0];
            y = coordinates[1];
            z = coordinates[2];
            p = coordinates[3];

            // if (y < 0) x = x + y
            // Gets lowest common
            // ancestor.
            x = y < 0 ? x + y : x;

            if (z > 0) {
                return finalRelationship(relationships,
                        "Not Blood-Related");
            }
            if (p > 1) {
                return finalRelationship(relationships,
                        "Not Related, but a Relative is Married to their Relative");
            } else if (p == 1) {
                inLaw = true;
            }

            // x == 0 && y < 0      :: n-PARENT
            // x == 1 && y < 0      :: n-AUNT_OR_UNCLE
            // x >= 1 && y == 0     :: SIBLING or n-COUSIN
            // x == y && y == 0      :: SPOUSE
            // x == y && y > 0      :: n-CHILD
            // x >= 2 && y == x - 1 :: n-NIECE_OR_NEPHEW
            // x >= 2 && y < 0      :: DESCENDANT_COUSIN, x-REMOVED
            // x >= y + 2 && y > 0  :: ASCENDANT_COUSIN, y-REMOVED

            if (isParent(x, y)) {
                // Linear: positive of Y
                // Constant: x
                y *= -1;
                if (y > 3) {
                    return finalRelationship(
                            relationships,
                            countRepeatedTimes(y - 2,
                                    inLaw,
                                    gender,
                                    GREAT_GRANDPARENT));
                }
                return finalRelationship(
                        relationships,
                        countInLaw(inLaw,
                                gender,
                                PARENT_COORDS[y - 1]));
            }
            if (isAuntUncle(x, y)) {
                // Linear: positive of y
                // Constant: x
                y *= -1;
                if (y > 3) {
                    return finalRelationship(
                            relationships,
                            countRepeatedTimes(y - 2,
                                    inLaw,
                                    gender,
                                    GREAT_GRANDAUNT_OR_UNCLE));
                }
                return finalRelationship(
                        relationships,
                        countInLaw(inLaw,
                                gender,
                                AUNT_UNCLE_COORDS[y - 1]));
            }
            if (isSiblingCousin(x, y)) {
                // Linear: x
                // Constant: y
                if (x > 2) {
                    return finalRelationship(
                            relationships,
                            countRepeatedTimes(x - 1,
                                    inLaw,
                                    gender,
                                    COUSIN));
                }
                return finalRelationship(
                        relationships,
                        countInLaw(inLaw,
                                gender,
                                SIBLING_COORDS[x - 1]));
            }
            if (isSpouse(x, y)) {
                // Constant: x
                // Constant: y
                return finalRelationship(
                        relationships,
                        getRelationship(gender, SPOUSE));
            }
            if (isChild(x, y)) {
                // Linear: x
                // Linear: y
                if (y > 3) {
                    return finalRelationship(
                            relationships,
                            countRepeatedTimes(
                                    y - 2,
                                    inLaw,
                                    gender,
                                    GREAT_GRANDCHILD));
                }
                return finalRelationship(
                        relationships,
                        countInLaw(
                                inLaw,
                                gender,
                                CHILD_COORDS[x - 1]));
            }
            if (isNieceNephew(x, y)) {
                // Linear: x
                // Linear: y
                return finalRelationship(
                        relationships,
                        countInLaw(
                                inLaw,
                                gender,
                                NIECE_NEPHEW_COORDS[x - 2]));
            }
            if (isDescCousin(x, y)) {
                // Linear: x
                // Linear: positive of y
                return finalRelationship(
                        relationships,
                        countRepeatedTimes(
                                x - 1,
                                inLaw,
                                gender,
                                DESCENDANT_COUSIN));
            }
            if (isAscCousin(x, y)) {
                return finalRelationship(
                        relationships,
                        countRepeatedTimes(
                                x - 2,
                                inLaw,
                                gender,
                                ASCENDANT_COUSIN));
            }
        }
        return finalRelationship(relationships, "Not Related");
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * fall under the n-parent equation.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * fall under the n-parent equation.
     */
    private static boolean isParent(int x, int y) {
        return x == 0 && y < 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * fall under the n-aunt-or-uncle equation.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * fall under the n-parent equation.
     */
    private static boolean isAuntUncle(int x, int y) {
        return x == 1 && y < 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * fall under the sibling/nth-cousin equation.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * fall under the n-parent equation.
     */
    private static boolean isSiblingCousin(int x, int y) {
        return x >= 1 && y == 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * are the same and both 0, showing that they
     * are spouses.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * are the same and both 0, showing that they
     * are spouses.
     */
    private static boolean isSpouse(int x, int y) {
        return x == y && y == 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * fall under the n-child equation.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * fall under the n-child equation.
     */
    private static boolean isChild(int x, int y) {
        return x == y && y > 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * fall under the n-niece-nephew equation.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * fall under the n-niece-nephew equation.
     */
    private static boolean isNieceNephew(int x, int y) {
        return x >= 2 && y == x - 1;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * show that they are nth-descendant cousins.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * show that they are nth-descendant cousins.
     */
    private static boolean isDescCousin(int x, int y) {
        return x >= 2 && y < 0;
    }

    /**
     * Returns <tt>true</tt> if x and y coordinates
     * show that they are nth-ascendant cousins.
     *
     * @param x lowest common ancestor.
     * @param y generational difference.
     * @return <tt>true</tt> if x and y coordinates
     * show that they are nth-ascendant cousins.
     */
    private static boolean isAscCousin(int x, int y) {
        return x >= y + 2 && y > 0;
    }

    /**
     * Returns a concatenated String of the output
     * relationship between two people.
     * <p>
     * Shortcut used to include an Integer before
     * the "x" character and the output relationship
     * between two people.
     *
     * @param count        Integer before "x".
     * @param inLaw        "In-Law" String that shows
     *                     if the two people are
     *                     related over a marriage.
     * @param gender       to be infused into the
     *                     relationship.
     * @param relationship Relationship enum to be
     *                     infused with a Gender,
     *                     or not.
     * @return a concatenated String of the output
     * relationship between two people.
     */
    private static String countRepeatedTimes(int count,
                                             boolean inLaw,
                                             Gender gender,
                                             Relationship relationship) {
        return count + "x " + countInLaw(inLaw, gender, relationship);
    }

    /**
     * Returns a concatenated String of the output
     * relationship between two people.
     *
     * @param inLaw        is appended, if two
     *                     people are related over
     *                     a marriage.
     * @param gender       to be infused into the
     *                     relationship.
     * @param relationship Relationship enum to be
     *                     infused with a Gender,
     *                     or not.
     * @return a concatenated String of the output
     * relationship between two people.
     */
    private static String countInLaw(boolean inLaw,
                                     Gender gender,
                                     Relationship relationship) {
        String lawString = !inLaw ? "" : " In-Law";
        return getRelationship(gender, relationship) + lawString;
    }

    /**
     * Returns a String array, after adding one
     * last relationship to a List of String
     * and converting the list into the array.
     *
     * @param relationships List of relationships
     *                      already found between
     *                      two people.
     * @param relationship  last relationship
     *                      String  to be added
     *                      into the List.
     * @return a String array, after adding one
     * last relationship to a List of String
     * and converting the list into the array.
     */
    private static String[] finalRelationship(List<String> relationships,
                                              String relationship) {
        relationships.add(relationship);
        return relationships.toArray(new String[0]);
    }
}
