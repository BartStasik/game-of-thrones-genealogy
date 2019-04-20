package com.got.genealogy.core.processor.data;

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

import static com.got.genealogy.core.processor.data.InformationPool.getRelationship;

public class File {

    public static String[][] loadFile(String absolutePath) {

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
                if (line.length() != 0) {
                    trimmedFile.add(words);
                }
            }
            return trimmedFile.toArray(
                    new String[trimmedFile.size()][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] exportGVFile(String absolutePath, FamilyTree family) {
        try {
            PrintWriter fileWriter = new PrintWriter(absolutePath + ".gv");
            List<String> outputFile = new ArrayList<>();
            String arc = " -> ";

            writeLine(("digraph " + absolutePath + "{ rankdir=LR;\n size =\"8,5\""),
                    outputFile,
                    fileWriter);

            writeLine(("node [shape=rectangle] [color=navy];"),
                    outputFile,
                    fileWriter);

            AdjacencyList<Person, Relation> list = family.adjacencyListWeighted();

            list.getList().forEach((k, v) -> {
                for (WeightedVertex<Person, Relation> person : v) {
                    Relation relation = person.getValue();
                    Gender gender = k.getGender();
                    String relationship = getRelationship(gender, relation);

                    String relationshipLine = String.format("\"%s\"%s\"%s\" [ label = \"%s\"];",
                            k.getLabel(),
                            arc,
                            person.getKey().getLabel(),
                            relationship);

                    writeLine(relationshipLine,
                            outputFile,
                            fileWriter);
                }
            });

            writeLine("}",
                    outputFile,
                    fileWriter);

            fileWriter.close();

            return outputFile.toArray(new String[0]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean exportSortedFile(String absolutePath, FamilyTree family) {
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

    private static void writeLine(String line, List<String> collector, PrintWriter writer) {
        writer.println(line);
        collector.add(line);
    }
}
