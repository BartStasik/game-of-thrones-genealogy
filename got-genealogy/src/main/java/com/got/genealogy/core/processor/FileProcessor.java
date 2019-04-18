package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import static com.got.genealogy.core.family.person.Gender.*;

public class FileProcessor {

    private FamilyTree family = new FamilyTree();

    public boolean loadFile(String absolutePath) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(absolutePath));
            String[] values;
            String line;

            while ((line = br.readLine()) != null) {
                values = line.split(", ");

                for (String s : values) {
                    s.trim();

                }

                Gender gender;

                switch (values[1].toUpperCase()) {
                    case "MALE":
                    case "FATHER":
                    case "MAN":
                        gender = MALE;
                        break;

                    case "WOMAN":
                    case "MOTHER":
                    case "FEMALE":
                        gender = MALE;

                    default:
                        gender = UNSPECIFIED;

                }


//                if (values[1].toUpperCase() == "MALE" || values[1].toUpperCase() == "FATHER") {
//                    gender = MALE;
//                } else if (values[1].toUpperCase() == "WOMAN" || values[1].toUpperCase() == "MOTHER") {
//                    gender = FEMALE;
//                } else{
//                    gender = UNSPECIFIED;
//                }

                switch (values.length) {
                    case 1:
                        if (family.getPerson(values[0]) == null) {
                            family.addPerson(values[0]);
                        }
                        break;
                    case 2:
                        if (family.getPerson(values[0]) == null) {
                            family.addPerson(values[0], gender);
                        }

                        break;
                    case 3:
                        if (family.getPerson(values[0]) == null) {
                            family.addPerson(values[0], gender);
                        }


                        if (family.getPerson(values[2]) == null) {
                            family.addPerson(values[2], gender);
                        }

                        family.addRelation(values[0], values[2], new Relation(values[1]));
                        break;
                    default:
                        System.out.println("INCORRECT FILE FORMAT");
                        return false;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean exportFile(String file) {

        PrintWriter writer;
        try {

            writer = new PrintWriter(file+ ".gv");

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        writer.println("digraph " + file + "{ rankdir=LR;\n size =\"8,5\"");
        writer.println("node [shape=circle] [color=black];");

        String arc = " -> ";

        AdjacencyList<Person, Relation> list = family.adjacencyListWeighted();
        list.getList().forEach((k, v) -> {
            System.out.print(k.getLabel() + " : ");


            for (WeightedVertex<Person, Relation> person : v) {
                System.out.print(person.getKey().getLabel() + ", ");
                writer.print("\"");
                writer.print(k.getLabel());
                writer.print("\"");
                writer.print(arc);
                writer.print("\"");
                writer.print(person.getKey().getLabel());
                writer.print("\"");
                writer.println(" [ label = \"" + list.getWeightedVertex(k,person.getKey()).getValue().getLabel() + "\"];");
            }
            System.out.println();
        });


        writer.println("}");
        writer.close();
        return true;
    }


    public FamilyTree getFamily() {
        return family;
    }
}
