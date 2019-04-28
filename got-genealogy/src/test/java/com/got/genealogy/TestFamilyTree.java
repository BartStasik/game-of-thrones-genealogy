package com.got.genealogy;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.*;
import static com.got.genealogy.core.family.person.Relationship.PARENT;
import static com.got.genealogy.core.family.person.Relationship.SPOUSE;
import static com.got.genealogy.core.processor.Genealogy.*;
import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;
import static com.got.genealogy.core.processor.data.FileHandler.decodeURL;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;
import static org.junit.jupiter.api.Assertions.fail;

public class TestFamilyTree {

    @Test
    void gotStarkTest() {
        GraphUtils<Person, Relation> graphUtils = new GraphUtils<>();
        FamilyTree family = new FamilyTree("Stark");
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

        family.addPerson("Robb Stark", MALE);

        // Both mother/father will be generic
        // and gender will be injected when
        // getting.
        // Eddard Stark, father
        family.addRelation("Eddard Stark",
                "Arya Stark",
                PARENT);
        family.addRelation("Eddard Stark",
                "Robb Stark",
                PARENT);
        family.addRelation("Eddard Stark",
                "Sansa Stark",
                PARENT);
        family.addRelation("Eddard Stark",
                "Bran Stark",
                PARENT);

        family.addRelation("Eddard Stark",
                "Catelyn Tully",
                SPOUSE);

        // Catelyn Tully, mother
        family.addRelation("Catelyn Tully",
                "Arya Stark",
                PARENT);
        family.addRelation("Catelyn Tully",
                "Robb Stark",
                PARENT);
        family.addRelation("Catelyn Tully",
                "Sansa Stark",
                PARENT);
        family.addRelation("Catelyn Tully",
                "Bran Stark",
                PARENT);
        family.addRelation("Catelyn Tully",
                "Rickon Stark",
                PARENT);

        // Rickard Stark, father
        family.addRelation("Rickard Stark",
                "Eddard Stark",
                PARENT);
        family.addRelation("Rickard Stark",
                "Lyanna Stark",
                PARENT);

        // Lyanna Stark, mother
        family.addRelation("Lyanna Stark",
                "Jon Snow",
                PARENT);

        // Rhaegar Targaryen, father
        family.addRelation("Rhaegar Targaryen",
                "Jon Snow",
                PARENT);

        // Rhaegar Targaryen, father
        family.addExtraRelation("Rhaegar Targaryen",
                "Rickon Stark",
                "Tests out");

        // Both mother and father are
        // 6 characters long.
        graphUtils.printGraph(family);

        System.out.println();

        graphUtils.printList(family.adjacencyListWeighted());

        System.out.println();
        List<Person> people = new ArrayList<>(family.getVertices().keySet());
        people.forEach((e) -> System.out.println(e.getLabel()));

        Collections.sort(people);
        System.out.println();
        people.forEach((e) -> System.out.println(e.getLabel()));

        System.out.println();
        int[] coordinates = family.calculateRelationCoords(
                family.getPerson("Rickon Stark"),
                family.getPerson("Eddard Stark"));
        System.out.printf("[%s, %s, %s, %s]",
                coordinates[0],
                coordinates[1],
                coordinates[2],
                coordinates[3]);
        System.out.println();
    }

    @Test
    void genericFamilyTest() {
        GraphUtils<Person, Relation> graphUtils = new GraphUtils<>();
        boolean testPassed = true;
        int i = 0;

        InputStream testResource = decodeResource("RelationshipTestFile.txt");
        InputStream testDetailsResource = decodeResource("PersonDetailsTestFile.txt");
        URL sourceCodeLocation = TestFamilyTree.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        if (testResource == null || testDetailsResource == null) {
            fail("Correct resource files don't exist!");
        }
        File file = new File(sourceCodeLocation.getFile());
        String sourceCodePath = decodeURL(file.getParent() + File.separator);

        // Disregarding case. Different
        // familyName case is intentional.
        loadRelationsFile(testResource, "TEST");
        loadPersonDetailsFile(testDetailsResource, "Test");
        exportDOT(sourceCodePath + "TestDOT", "Test");
        exportSorted(sourceCodePath + "TestSorted", "Test");

        FamilyTree testFamily = getFamily("Test");

        if (testFamily == null) {
            fail("No family named \"Test\"!");
        }

        // Shouldn't add this person.
        // Disregarding case.
        testFamily.addPerson("bartosz StAsIk");

        graphUtils.printGraph(testFamily);

        List<Pair> relations = new ArrayList<>();

        // Connected by father - Luke Puckett
        relations.add(new Pair("Ebony Mohamed", "Bilal Rios"));
        relations.add(new Pair("Ebony Mohamed", "Leonardo Love"));
        relations.add(new Pair("Ebony Mohamed", "Brenda Torres"));
        relations.add(new Pair("Ebony Mohamed", "Luke Puckett"));
        relations.add(new Pair("Ebony Mohamed", "Tasmin Williamson"));
        relations.add(new Pair("Ebony Mohamed", "Sophie-Louise Lu"));

        // Connected by grandmother - Khia Kemp
        relations.add(new Pair("Ebony Mohamed", "Khia Kemp"));
        relations.add(new Pair("Ebony Mohamed", "Chanice Mcintyre"));
        relations.add(new Pair("Ebony Mohamed", "Samson Fellows"));
        relations.add(new Pair("Ebony Mohamed", "Reegan Serrano"));
        relations.add(new Pair("Ebony Mohamed", "Alysia Weeks"));

        // Connected by great-grandmother - Nina Barnard
        relations.add(new Pair("Ebony Mohamed", "Nina Barnard"));
        relations.add(new Pair("Ebony Mohamed", "Rex Roy"));
        relations.add(new Pair("Ebony Mohamed", "Rhia Mccarty"));
        relations.add(new Pair("Ebony Mohamed", "Stacie Fountain"));
        relations.add(new Pair("Ebony Mohamed", "Poppy-Rose Harmon"));
        relations.add(new Pair("Ebony Mohamed", "Dolly Wyatt"));
        relations.add(new Pair("Ebony Mohamed", "Carys Hubbard"));

        // Connected by 2x great-grandmother - Katie Crowther
        relations.add(new Pair("Ebony Mohamed", "Katie Crowther"));
        relations.add(new Pair("Ebony Mohamed", "Kayden Beach"));
        relations.add(new Pair("Ebony Mohamed", "Star Wharton"));
        relations.add(new Pair("Ebony Mohamed", "Dru Mercado"));
        relations.add(new Pair("Ebony Mohamed", "Mila-Rose Chaney"));
        relations.add(new Pair("Ebony Mohamed", "Grace Montes"));
        relations.add(new Pair("Ebony Mohamed", "Jake Pearson"));

        // No path
        relations.add(new Pair("Ebony Mohamed", "Bartosz Stasik"));

        // x In-Law
        relations.add(new Pair("Ebony Mohamed", "Ahmed Iqbal"));

        // Not related, but someone in the family is married with their relative in-law
        relations.add(new Pair("Ebony Mohamed", "Josh Button"));
        relations.add(new Pair("Ebony Mohamed", "Ashleigh More Hattia"));

        // Not blood related
        relations.add(new Pair("Ebony Mohamed", "Jon Do"));

        // Spouse, Likes
        relations.add(new Pair("Ahmed Iqbal", "Nina Barnard"));

        // Loves
        relations.add(new Pair("Nina Barnard", "Ebony Mohamed"));

        // Hates
        relations.add(new Pair("nina barnard", "bartosz Stasik"));

        String[][] expectedRelationshipsInOrder = new String[][]{
                new String[]{"Grandaunt"},
                new String[]{"Aunt"},
                new String[]{"Sister"},
                new String[]{"Daughter"},
                new String[]{"Mother"},
                new String[]{"Grandmother"},
                new String[]{"Granddaughter"},
                new String[]{"Niece"},
                new String[]{"Niece"},
                new String[]{"Cousin"},
                new String[]{"1x Descendant Cousin"},
                new String[]{"Great Granddaughter"},
                new String[]{"Grandniece"},
                new String[]{"1x Ascendant Cousin"},
                new String[]{"2x Cousin"},
                new String[]{"2x Descendant Cousin"},
                new String[]{"2x Descendant Cousin"},
                new String[]{"2x Descendant Cousin"},
                new String[]{"2x Great Granddaughter"},
                new String[]{"Great Grandniece"},
                new String[]{"2x Ascendant Cousin"},
                new String[]{"2x Ascendant Cousin"},
                new String[]{"3x Cousin"},
                new String[]{"3x Descendant Cousin"},
                new String[]{"3x Descendant Cousin"},
                new String[]{"Not Related"},
                new String[]{"Great Granddaughter In-Law"},
                new String[]{"Not Related, but a Relative is Married to their Relative"},
                new String[]{"Not Related, but a Relative is Married to their Relative"},
                new String[]{"Not Blood-Related"},
                new String[]{"Likes", "Husband"},
                new String[]{"Loves", "Great Grandmother"},
                new String[]{"Kind Of Hates", "Not Related"}
        };

        String[] expectedPeopleForDetails = new String[]{
                "Bilal Rios",
                "Leonardo Love",
                "Brenda Torres",
                "Non Connected Dude"
        };

        for (Pair e : relations) {
            String[] relationships = findRelationship(
                    e.key,
                    e.value,
                    "Test");
            if (relationships == null) {
                testPassed = false;
                System.out.println("Error");
                break;
            }
            for (String relationship : relationships) {
                boolean correctRelationship = false;
                for (String extraRelationship : expectedRelationshipsInOrder[i]) {
                    correctRelationship = relationship.equals(extraRelationship);
                    if (correctRelationship) {
                        break;
                    }
                }
                if (!correctRelationship) {
                    testPassed = false;
                }
                System.out.printf("%n%s -> %s%n%b : %s%n<expected : %s>%n",
                        e.key,
                        e.value,
                        correctRelationship,
                        relationship,
                        Arrays.toString(expectedRelationshipsInOrder[i]));
            }
            i++;
        }

        for (String person : expectedPeopleForDetails) {
            // Setting to lowercase, to ensure
            // it's not case sensitive
            Map<String, String> details = getPersonDetails(
                    person.toLowerCase(),
                    "Test");
            if (details == null) {
                testPassed = false;
            } else {
                System.out.println("\n" + person);
                details.forEach((k, v) -> {
                    System.out.println(toTitleCase(k) + " : " + toTitleCase(v));
                });
            }
        }

        String[] allPeople = getAllPeople("Test");

        if (allPeople == null) {
            fail("No family named \"Test\"!");
            return;
        }

        System.out.println();

        for (String person : allPeople) {
            System.out.println(person);
        }

        if (!testPassed) {
            fail("Invalid family relationships!");
        }
    }

    @Test
    void gotFamilyTest() {
        InputStream testGenealogyTree = decodeResource("GenealogyTree.txt");
        InputStream testDetailsResource = decodeResource("PersonDetails.txt");
        URL sourceCodeLocation = TestFamilyTree.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        String sourceCodePath = new File(sourceCodeLocation.getFile())
                .getParent() + File.separator;

        if (testGenealogyTree == null || testDetailsResource == null) {
            fail("Correct resource files don't exist!");
        }

        loadRelationsFile(testGenealogyTree, "fullTree");
        loadPersonDetailsFile(testDetailsResource, "fullTree");
        exportDOT(sourceCodePath + "TestGenealogyDOT", "fullTree");
        exportSorted(sourceCodePath + "TestGenealogySorted", "fullTree");

        String[] s = findRelationship(
                "Joanna Lannister",
                "Lancel Lannister",
                "fullTree");
        if (s == null) {
            fail("Either person or family name doesn't exist!");
        }
        for (String value : s) {
            System.out.println("Relation: " + value);
        }
        System.out.println();

        Map<String, String> map;
        map = getPersonDetails("Rhaego", "fullTree");
        if (map == null) {
            fail("Either person or family name doesn't exist!");
        }
        map.forEach((k, v) -> {
            System.out.println(toTitleCase(k) + " : " + toTitleCase(v));
        });
    }
}
