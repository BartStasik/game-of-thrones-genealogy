package com.got.genealogy;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Weight;

import java.io.File;
import java.net.URL;
import java.util.*;

import static com.got.genealogy.core.processor.Genealogy.*;


public class TestFileProcessor {

    public static void main(String[] args) {
        boolean testPassed = true;
        int i = 0;

        String testRelationPath;

        URL testResource = TestFileProcessor.class
                .getClassLoader()
                .getResource("RelationshipTestFile.txt");

        if (testResource == null) {
            return;
        }
        testRelationPath = new File(testResource.getFile()).getAbsolutePath();

        loadRelation(testRelationPath, "Test");
        exportDOT("TestDOT", "Test");
        exportSorted("TestSorted.txt", "Test");

        FamilyTree testFamily = getFamily("Test");

        class Pair {
            private String key, value;
            private Pair(String k, String v){
                key = k;
                value = v;
            }
        }

        printGraph(testFamily);

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


        String[] expectedRelationshipsInOrder = new String[]{
                "Grandaunt",
                "Aunt",
                "Sister",
                "Daughter",
                "Mother",
                "Grandmother",
                "Granddaughter",
                "Niece",
                "Niece",
                "Cousin",
                "1x Descendant Cousin",
                "Great Granddaughter",
                "Grandniece",
                "1x Ascendant Cousin",
                "2x Cousin",
                "2x Descendant Cousin",
                "2x Descendant Cousin",
                "2x Descendant Cousin",
                "2x Great Granddaughter",
                "Great Grandniece",
                "2x Ascendant Cousin",
                "2x Ascendant Cousin",
                "3x Cousin",
                "3x Descendant Cousin",
                "3x Descendant Cousin",
                "Not Related",
                "Great Granddaughter In-Law",
                "Not Related, but someone in family is married to their relative",
                "Not Related, but someone in family is married to their relative",
                "Not Blood-Related"
        };

        for (Pair e : relations) {
            String relationship = findRelationship(e.key, e.value, "Test");
            if (relationship == null) {
                testPassed = false;
                System.out.println("Invalid Family Name");
                break;
            }
            boolean correctRelationship = relationship.equals(expectedRelationshipsInOrder[i]);
            if (!correctRelationship) {
                testPassed = false;
            }
            System.out.printf("%s -> %s%n%b : %s%n",
                    e.key,
                    e.value,
                    correctRelationship,
                    relationship);
            i++;
        }

        if (!testPassed) {
            System.out.println("\nTEST FAILED");
        } else {
            System.out.println("\nTEST PASSED");
        }
    }


    private static void printGraph(FamilyTree graph) {
        AdjacencyMatrix<Weight<Relation>> matrix = graph.getAdjacencyMatrix();
        Map<Person, Integer> vertices = graph.getVertices();

        int size = matrix.size();
        // Print column labels
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            for (Map.Entry<Person, Integer> vertex : vertices.entrySet()) {
                // TODO: Replace with LinkedHashMap
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
        }
        // Print matrix
        System.out.println();
        for (int i = 0; i < size; i++) {
            // HashMap isn't ordered, so
            // get correct vertex label
            // and print with space.
            for (Map.Entry<Person, Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            // Print weights for vertices,
            // for this row.
            for (int j = 0; j < size; j++) {
                String spacer = (j != size - 1) ? ", " : "";
                Weight<Relation> weight = matrix.getCell(i, j);
                Object weightValue;
                // Allow for custom operation,
                // e.g. get something other
                // than label.
                if (weight != null) {
                    weightValue = weight.getWeight()
                            .getLabel();
                } else {
                    weightValue = "______";
                }
                System.out.print(weightValue + spacer);
            }
            System.out.println();
        }
    }
}
