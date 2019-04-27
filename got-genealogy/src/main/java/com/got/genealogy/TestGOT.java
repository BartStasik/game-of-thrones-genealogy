package com.got.genealogy;

import java.io.File;
import java.net.URL;
import java.util.Map;

import static com.got.genealogy.core.processor.Genealogy.*;
import static com.got.genealogy.core.processor.Genealogy.exportSorted;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;

public class TestGOT {
    public static void main(String[] args) {

        String testGenealogyPath, testDetailsPath, sourceCodePath;

        URL testGenealogyTree = TestFileProcessor.class
                .getClassLoader()
                .getResource("GenealogyTree.txt");

        URL testDetailsResource = TestFileProcessor.class
                .getClassLoader()
                .getResource("PersonDetails.txt");

        URL sourceCodeLocation = TestFileProcessor.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        testDetailsPath = new File(testDetailsResource.getFile())
                .getAbsolutePath();

        testGenealogyPath = new File(testGenealogyTree.getFile())
                .getAbsolutePath();

        sourceCodePath = new File(sourceCodeLocation.getFile())
                .getParent() + File.separator;

        loadRelationsFile(testGenealogyPath, "fullTree");
        loadPersonDetailsFile(testDetailsPath, "fullTree");

        exportDOT(sourceCodePath + "TestGenealogyDOT", "fullTree");
        exportSorted(sourceCodePath + "TestGenealogySorted", "fullTree");

        String[] s = findRelationship("Joanna Lannister", "Lancel Lannister", "fullTree");

        for (int a = 0; a < s.length; a++) {
            System.out.println("Relation: " + s[a]);

        }
        System.out.println();
        Map<String, String> map;
        map = getPersonDetails("Rhaego", "fullTree");

            map.forEach((k, v) -> {
                System.out.println(toTitleCase(k) + " : " + toTitleCase(v));
            });
        }

    }
