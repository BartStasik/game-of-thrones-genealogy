package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileProcessor {

    public String[][] loadFile(String absolutePath) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(absolutePath));
            List<String[]> trimmedFile = new ArrayList<>();
            String[] words;
            String line;

            while ((line = br.readLine()) != null) {
                words = line.split(",");

                for (int i =0; i<words.length; i++) {
                    words[i] = words[i].replaceAll("\\s{2,}", " ").trim();
                }
                trimmedFile.add(words);

            }
            String[][] cleanFile = new String[trimmedFile.size()][];

            return trimmedFile.toArray(cleanFile);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean exportGVFile(String absolutePath, FamilyTree family) {

        PrintWriter writer;
        try {
            writer = new PrintWriter(absolutePath + ".gv");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        writer.println("digraph " + absolutePath + "{ rankdir=LR;\n size =\"8,5\"");
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
                writer.println(" [ label = \"" + list.getWeightedVertex(k, person.getKey()).getValue().getLabel() + "\"];");
            }
            System.out.println();
        });

        writer.println("}");
        writer.close();
        return true;
    }

    public void sortPeople(FamilyTree family){
        ArrayList<Person> sortedPeople = new ArrayList<>(family.getVertices().keySet());
        Collections.sort(sortedPeople);
        PrintWriter writer;
        try {
            writer = new PrintWriter("Sorted People.txt");
            sortedPeople.forEach((e) -> System.out.println(e.getLabel()));
            sortedPeople.forEach((e) -> writer.println(e.getLabel()));
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
