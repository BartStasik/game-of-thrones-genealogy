package com.got.genealogy.core.processor;

import com.got.genealogy.core.family.person.Gender;
import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.family.FamilyTree;

import java.io.BufferedReader;
import java.io.FileReader;

import static com.got.genealogy.core.family.person.Gender.*;

public class FileProcessor {

    private FamilyTree family = new FamilyTree();

    public int loadFile(String absolutePath) {

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

                if (values[1].toUpperCase() == "MALE" || values[1].toUpperCase() == "FATHER") {
                    gender = MALE;
                } else if (values[1].toUpperCase() == "WOMAN" || values[1].toUpperCase() == "MOTHER") {
                    gender = FEMALE;
                } else{
                    gender = UNSPECIFIED;
                }

                switch (values.length) {
                    case 1:
                        //code
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
                }
            }


        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public FamilyTree getFamily() {
        return family;
    }
}
