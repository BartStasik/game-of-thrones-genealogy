package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.got.genealogy.core.family.person.Relationship.*;
import static com.got.genealogy.core.processor.data.File.exportGVFile;
import static com.got.genealogy.core.processor.data.File.exportSortedFile;
import static com.got.genealogy.core.processor.data.File.loadFile;
import static com.got.genealogy.core.processor.data.InformationPool.*;

public class Genealogy {

    private static List<FamilyTree> families = new ArrayList<>();

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

    public static FamilyTree getFamily(int familyIndex) {
        return families.get(familyIndex);
    }

    public static void setFamily(int familyIndex, FamilyTree familyTree) {
        families.set(familyIndex, familyTree);
    }

    public static void addFamily(FamilyTree familyTree) {
        families.add(familyTree);
    }

    public static void removeFamily(String familyName) {
        FamilyTree family = getFamily(familyName);
        if (family != null) {
            removeFamily(family);
        }
    }

    public static void removeFamily(int familyIndex) {
        families.remove(familyIndex);
    }

    public static void removeFamily(FamilyTree familyTree) {
        families.remove(familyTree);
    }

    public static String findRelationship(String name1, String name2, String familyName) {
        FamilyTree family = getFamily(familyName);
        if (family == null) {
            return null;
        }

        Person person1 = family.getPerson(name1);
        Person person2 = family.getPerson(name2);

        if (person1 == person2 || person1 == null || person2 == null){
            return null;
        }

        return processRelationship(person1, person2, family);
    }

    public static FamilyTree loadRelation(String absolutePath, String familyName) {
        try {
            String[][] file = loadFile(absolutePath);
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
                        gender = getGender(row[1]);
                        family.addPerson(name1, gender);
                        break;
                    case 3:
                        name1 = row[0];
                        name2 = row[2];
                        gender = getGender(row[1]);
                        relationship = getRelationship(row[1]);

                        if (relationship != null) {
                            family.addPerson(name1, gender);
                            family.addPerson(name2);
                            if (relationship.equals(CHILD)) {
                                family.addRelation(name2, name1, new Relation(PARENT));
                            } else {
                                family.addRelation(name1, name2, new Relation(relationship));
                            }
                        } else {
                            family.addPerson(name1);
                            family.addPerson(name2);
                            family.addRelation(name1, name2, new Relation(row[1]));
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

    public static String[] exportDOT(String absolutePath, String familyName) {
        FamilyTree family = getFamily(familyName);
        return family == null ? null : exportGVFile(absolutePath, family);
    }

    public static boolean exportSorted(String absolutePath, String familyName) {
        FamilyTree family = getFamily(familyName);
        return family != null && exportSortedFile(absolutePath, family);
    }

    private static String processRelationship(Person person1, Person person2, FamilyTree family) {
        int x, y, z, p, grandTimes, cousinTimes;
        int[] coordinates;

        Relation direct = family.getRelation(person1, person2);
        Gender gender = person1.getGender();
        String inLaw = "";

        if (direct != null) {
            return getRelationship(gender, direct);
        }

        coordinates = family.calculateRelationCoords(
                person1,
                person2);

        if (coordinates == null) {
            return "Not Related";
        }

        x = coordinates[0];
        y = coordinates[1];
        z = coordinates[2];
        p = coordinates[3];

        x = y < 0 ? x + y : x;

        if (z > 0) {
            return "Not Blood-Related";
        }
        if (p > 1) {
            return "Not Related, but someone in family is married to their relative";
        } else if (p == 1) {
            inLaw = " In-Law";
        }

        // x == 0 && y < 0      :: n-PARENT
        // x == 1 && y < 0      :: n-AUNT_OR_UNCLE
        // x >= 1 && y == 0     :: SIBLING or n-COUSIN
        // x == y && y = 0      :: SPOUSE
        // x == y && y > 0      :: n-CHILD
        // x >= 2 && y == x - 1 :: n-NIECE_OR_NEPHEW
        // x >= 2 && y < 0      :: DESCENDANT_COUSIN, x-REMOVED
        // x >= y + 2 && y > 0  :: ASCENDANT_COUSIN, y-REMOVED
        if (x == 0 && y < 0) {
            // Linear: positive of Y
            // Constant: x
            y *= -1;
            grandTimes = y-2;
            if (y > 3) {
                return grandTimes + "x " + getRelationship(gender, GREAT_GRANDPARENT);
            }
            return getRelationship(gender, COORD_PARENT_RELATIONSHIPS[y-1]) + inLaw;
        }
        if (x == 1 && y < 0) {
            // Linear: positive of Y
            // Constant: x
            y *= -1;
            grandTimes = y-2;
            if (y > 3) {
                return grandTimes + "x " + getRelationship(gender, GREAT_GRANDAUNT_OR_UNCLE);
            }
            return getRelationship(gender, COORD_AUNT_UNCLE_RELATIONSHIPS[y-1]) + inLaw;
        }
        if (x >= 1 && y == 0) {
            // Linear: x
            // Constant: y
            cousinTimes = x - 1;
            if (x > 2) {
                return cousinTimes + "x " + getRelationship(gender, COUSIN);
            }
            return getRelationship(gender, COORD_SIBLING_RELATIONSHIPS[x-1]) + inLaw;
        }
        if (x == y && y == 0) {
            // Constant: x
            // Constant: y
            return getRelationship(gender, SPOUSE) + inLaw;
        }
        if (x == y && y > 0) {
            // Linear: x
            // Linear: y
            grandTimes = y-2;
            if (y > 3) {
                return grandTimes + "x " + getRelationship(gender, GREAT_GRANDCHILD);
            }
            return getRelationship(gender, COORD_CHILD_RELATIONSHIPS[x-1]) + inLaw;
        }
        if (x >= 2 && y == x - 1) {
            // Linear: x
            // Linear: y
            return getRelationship(gender, COORD_NIECE_NEPH_RELATIONSHIPS[x-2]) + inLaw;
        }
        if (x >= 2 && y < 0) {
            // Linear: x
            // Linear: positive of Y
            y *= -1;
            cousinTimes = x - 1;
            return cousinTimes + "x " + getRelationship(gender, DESCENDANT_COUSIN) + inLaw;
        }
        if (x >= y + 2 && y > 0) {
            cousinTimes = x - 2;
            return cousinTimes + "x " + getRelationship(gender, ASCENDANT_COUSIN) + inLaw;
        }

        return "Not Related";
    }
}
