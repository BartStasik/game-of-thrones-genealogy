package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.got.genealogy.core.processor.InformationPool.getRelationship;

class FileProcessor {

    static String[][] loadFile(String absolutePath) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(absolutePath));
            List<String[]> trimmedFile = new ArrayList<>();
            String[] words;
            String line;

            while ((line = br.readLine()) != null) {
                words = line.split(",");

                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].replaceAll("\\s{2,}", " ").trim();
                }
                trimmedFile.add(words);

                if (line.length() == 0) {
                    return null;
                }
            }
            String[][] cleanFile = new String[trimmedFile.size()][];
            return trimmedFile.toArray(cleanFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean exportGVFile(String absolutePath, FamilyTree family) {
        String arc = " -> ";
        PrintWriter writer;

        try {
            writer = new PrintWriter(absolutePath + ".gv");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        writer.println("digraph " + absolutePath + "{ rankdir=LR;\n size =\"8,5\"");
        writer.println("node [shape=rectangle] [color=navy];");

        AdjacencyList<Person, Relation> list = family.adjacencyListWeighted();
        list.getList().forEach((k, v) -> {

            for (WeightedVertex<Person, Relation> person : v) {
                Relation relation = person.getValue();
                Gender gender = k.getGender();
                String output = getRelationship(gender, relation);

                writer.print("\"");
                writer.print(k.getLabel());
                writer.print("\"");
                writer.print(arc);
                writer.print("\"");
                writer.print(person.getKey().getLabel());
                writer.print("\"");
                writer.println(" [ label = \"" + output + "\"];");
            }
        });

        writer.println("}");
        writer.close();
        return true;
    }

    static boolean exportSortedFile(String absolutePath, FamilyTree family) {
        ArrayList<Person> sortedPeople = new ArrayList<>(family.getVertices().keySet());
        Collections.sort(sortedPeople);
        PrintWriter writer;
        try {
            writer = new PrintWriter(absolutePath);
            sortedPeople.forEach((e) -> writer.println(e.getLabel()));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
