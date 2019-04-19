package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Person;
import com.got.genealogy.core.family.person.Relation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.got.genealogy.core.family.person.Gender.*;

public class GenealogyProcessor {

    List<FamilyTree> families = new ArrayList<>();
    private FamilyTree family = new FamilyTree();

    public FamilyTree loadRelation(String[][] file) {
        try {

            for(int i=0; i<file.length; i++){
                for(int j=0; j<file[i].length; j++){
                    System.out.print(file[i][j]);
                }
                System.out.println();
            }

                Gender gender;

                for(String[] row : file ) {

                    switch (row[1].toUpperCase()) {
                        case "MALE":
                        case "FATHER":
                        case "MAN":
                            gender = MALE;
                            break;

                        case "WOMAN":
                        case "MOTHER":
                        case "FEMALE":
                            gender = FEMALE;

                        default:
                            gender = UNSPECIFIED;
                    }

                    switch (row.length) {
                        case 1:
                            if (family.getPerson(row[0]) ==null){
                            family.addPerson(row[0]);
                        }
                        break;
                        case 2:
                            if (family.getPerson(row[0]) == null) {
                                family.addPerson(row[0], gender);
                            }

                            break;
                        case 3:
                            if (family.getPerson(row[0]) == null) {
                                family.addPerson(row[0], gender);
                            }


                            if (family.getPerson(row[2]) == null) {
                                family.addPerson(row[2], gender);
                            }

                            family.addRelation(row[0], row[2], new Relation(row[1]));
                            break;
                        default:
                            System.out.println("INCORRECT FILE FORMAT");
                            return null;
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return family;
    }
}
