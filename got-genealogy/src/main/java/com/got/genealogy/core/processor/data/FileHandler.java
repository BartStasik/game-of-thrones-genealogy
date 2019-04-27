package com.got.genealogy.core.processor.data;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.got.genealogy.core.processor.data.InformationPool.getRelationship;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;
import static com.got.genealogy.core.processor.data.StringUtils.writeFileExtension;

public class FileHandler {

    public static String[][] loadResourceFile(InputStream resourceStream) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(resourceStream));
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
        if (family == null) {
            return null;
        }
        try {
            String decodedURL = URLDecoder.decode(
                    writeFileExtension(absolutePath, ".gv"),
                    "UTF-8");
            PrintWriter fileWriter = new PrintWriter(decodedURL, "UTF-8");
            List<String> outputStorage = new ArrayList<>();

            writeLine(("digraph " + family.getLabel() + "{ rankdir=LR;\n size =\"8,5\""),
                    outputStorage,
                    fileWriter);

            writeLine(("node [shape=rectangle color=navy];"),
                    outputStorage,
                    fileWriter);

            AdjacencyList<Person, Relation> list = family.adjacencyListWeighted();

            list.getList().forEach((k, v) -> {
                for (WeightedVertex<Person, Relation> person : v) {
                    Relation relation = person.getValue();
                    Gender gender = k.getGender();
                    String relationship;

                    Set<String> extras = relation.getExtras();
                    if (extras.size() != 0) {
                        for (String extra : extras) {
                            writeRelationshipLine(
                                    k.getLabel(),
                                    person.getKey().getLabel(),
                                    toTitleCase(extra),
                                    outputStorage,
                                    fileWriter
                            );
                        }
                    }
                    if (!relation.getLabel().isEmpty()) {
                        relationship = getRelationship(gender, relation);
                        writeRelationshipLine(
                                k.getLabel(),
                                person.getKey().getLabel(),
                                relationship,
                                outputStorage,
                                fileWriter
                        );
                    }
                }
            });

            writeLine("}",
                    outputStorage,
                    fileWriter);

            fileWriter.close();

            return outputStorage.toArray(new String[0]);

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static Person[] exportSortedFile(String absolutePath, FamilyTree family) {
        if (family == null) {
            return null;
        }
        try {
            List<Person> sortedPeople = new ArrayList<>(family.getVertices().keySet());
            Collections.sort(sortedPeople);
            PrintWriter writer;
            String decodedURL = URLDecoder.decode(
                    writeFileExtension(absolutePath, ".txt"),
                    "UTF-8");
            writer = new PrintWriter(decodedURL, "UTF-8");
            sortedPeople.forEach((e) -> writer.println(e.getLabel()));
            writer.close();
            return sortedPeople.toArray(new Person[0]);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String decodeURL(String path) {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream decodeResource(String relativeResource) {
        InputStream resourceURL = FileHandler.class
                .getClassLoader()
                .getResourceAsStream(relativeResource);
        if (resourceURL == null) {
            return null;
        }
        return resourceURL;
    }

    private static void writeRelationshipLine(String person1,
                                              String person2,
                                              String relationship,
                                              List<String> collector,
                                              PrintWriter writer) {
        String relationshipLine = String.format("\"%s\"->\"%s\" [ label = \"%s\"];",
                person1,
                person2,
                relationship);
        writeLine(relationshipLine,
                collector,
                writer);
    }

    private static void writeLine(String line,
                                  List<String> collector,
                                  PrintWriter writer) {
        writer.println(line);
        collector.add(line);
    }
}
