package com.got.genealogy;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Weight;

import java.io.File;
import java.net.URL;
import java.util.Map;

import static com.got.genealogy.core.processor.GenealogyProcessor.*;


public class TestFileProcessor {

    public static void main(String[] args) {
        String sortedFile = "SortedPeople.txt";
        String relationFile = "InputFile.txt";
        String dotFile = "InputFile";
        String absoluteRelationPath;

        URL resource = TestFileProcessor.class
                .getClassLoader()
                .getResource(relationFile);

        if (resource == null) {
            return;
        }

        absoluteRelationPath = new File(resource.getFile()).getAbsolutePath();

        loadRelation(absoluteRelationPath, "Stark");
        exportDOT(dotFile, "Stark");
        exportSorted(sortedFile, "Stark");

        FamilyTree family = getFamily("Stark");

//        if (family != null) {
//            System.out.println();
//            int[] coordinates = family.calculateRelationCoords(family.getPerson("Catelyn Tully"), family.getPerson("Eddard Stark"));
//            System.out.printf("[%s, %s, %s, %s]", coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
//            System.out.println();
//        }

        printGraph(family);

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
