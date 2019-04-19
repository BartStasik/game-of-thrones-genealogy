package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.person.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.got.genealogy.core.family.person.Relationship.CHILD;
import static com.got.genealogy.core.family.person.Relationship.PARENT;
import static com.got.genealogy.core.processor.FileProcessor.exportGVFile;
import static com.got.genealogy.core.processor.FileProcessor.exportSortedFile;
import static com.got.genealogy.core.processor.FileProcessor.loadFile;
import static com.got.genealogy.core.processor.InformationPool.*;

public class GenealogyProcessor {

    private static List<FamilyTree> families = new ArrayList<>();

    public static FamilyTree getFamily(String familyName) {
        for (FamilyTree family : families) {
            if (family.getLabel().equals(familyName)) {
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

    public static boolean exportDOT(String absolutePath, String familyName) {
        FamilyTree family = getFamily(familyName);
        return family != null && exportGVFile(absolutePath, family);
    }

    public static boolean exportSorted(String absolutePath, String familyName) {
        FamilyTree family = getFamily(familyName);
        return family != null && exportSortedFile(absolutePath, family);
    }

}
