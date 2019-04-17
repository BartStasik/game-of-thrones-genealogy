package com.got.genealogy;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.FEMALE;
import static com.got.genealogy.core.family.person.Gender.MALE;
import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;

public class TestFamilyTree {

    public static void main(String[] args) {
        // Should eventually be in the
        // file FileProcessor or controller file?
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
        printGraph(family);

        System.out.println();

        printList(family.adjacencyListWeighted());

        System.out.println();
        List<Person> people = new ArrayList<>(family.vertices().keySet());
        people.forEach((e) -> System.out.println(e.getLabel()));

        Collections.sort(people);
        System.out.println();
        people.forEach((e) -> System.out.println(e.getLabel()));

    }

    private static void printList(AdjacencyList<Person, Relation> list) {
        list.getList()
                .forEach((k, v) -> {
                    // Set<WeightedVertex>
                    System.out.print(k.getLabel() + " : { ");
                    v.forEach((e) -> {
                        // WeightedVertex
                        System.out.printf("[%s:%s], ",
                                e.getKey().getLabel(),
                                e.getValue().getLabel());
                    });
                    System.out.println(" }");
                });
    }

    private static void printGraph(FamilyTree graph) {
        AdjacencyMatrix<Weight<Relation>> matrix = graph.adjacencyMatrix();
        Map<Person, Integer> vertices = graph.vertices();

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
