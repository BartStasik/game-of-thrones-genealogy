package com.got.genealogy;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import static com.got.genealogy.core.processor.Genealogy.exportDOT;
import static com.got.genealogy.core.processor.Genealogy.exportSorted;
import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.getPersonDetails;
import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;

public class TestGOT {

    public static void main(String[] args) {

        String sourceCodePath;

        InputStream testGenealogyTree = decodeResource("GenealogyTree.txt");

        InputStream testDetailsResource = decodeResource("PersonDetails.txt");

        URL sourceCodeLocation = TestGOT.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        sourceCodePath = new File(sourceCodeLocation.getFile())
                .getParent() + File.separator;

        loadRelationsFile(testGenealogyTree, "fullTree");
        loadPersonDetailsFile(testDetailsResource, "fullTree");

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
