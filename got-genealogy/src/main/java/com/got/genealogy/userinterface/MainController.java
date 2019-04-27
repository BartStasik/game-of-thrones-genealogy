package com.got.genealogy.userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.exportDOT;
import static com.got.genealogy.core.processor.Genealogy.exportSorted;
import static com.got.genealogy.core.processor.Genealogy.getAllPeople;
import static com.got.genealogy.core.processor.Genealogy.getPersonDetails;
import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class MainController {
    
    Stage primaryStage;

    String exportPath;
    FileChooser fileChooser = new FileChooser();
    ObservableList characters = FXCollections.observableArrayList();
    
    @FXML
    private Button loadButton;
    
    @FXML
    private AnchorPane anchorParent;
    
    @FXML 
    private ListView<String> characterSelect1;
    
    @FXML 
    private ListView<String> characterSelect2;

    @FXML
    private Label dispProfile;
    
    @FXML
    private Label character1;
        
    @FXML
    private Label character2;
    
    @FXML
    private Label dispExportResults;
    
    @FXML
    private Label dispProfile1;

    @FXML
    private Label dispField;
    
    // --------------------------- --------------------------- ---------------------------
    // FIND RELATIONSHIP METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void findRelation(ActionEvent event) {
        displayRelationship();
    }
    
    void displayRelationship(){
        String person1Name = character1.getText();
        String person2Name = character2.getText();
        
        if(character1.getText().equals("") || character2.getText().equals("")){
            return;
        } else if (character1.getText().equals(character2.getText())){
            dispField.setText("Is");
        } else{
            //Find relationship - method comes from Genealogy
            String[] relationship = 
                    findRelationship(person1Name, person2Name, "GOT");
            if (relationship == null) {
                return;
            }
            dispField.setText("");

            // Print out list of relationship attributes between two characters
            for (String realtionshipType : relationship) {
                dispField.setText(
                        dispField.getText()  
                        + realtionshipType
                        + "\n");
            }
        }
    }

    // --------------------------- --------------------------- ---------------------------
    // EXPORT FILE METHODS
    // --------------------------- --------------------------- ---------------------------


    @FXML
    void exportFamily(ActionEvent event) {
        // 1. File Chooser (showSaveDialog) - return path
        File exportPath = fileChooser.showSaveDialog(primaryStage);
        if(exportPath == null){
            return;
            //popup, what is cancel is pressed?
        }
        dispExportResults.setWrapText(true);
        dispExportResults.setText("Exporting to: " + exportPath + ".gv \n");
        // 2. export file using load method
        // 3. Display filepath and array of items that were exported on screen
        String[] fileOutput = exportDOT(exportPath
                .getAbsolutePath(),"GOT");
        String exportOutput = Arrays.toString(fileOutput);
        dispExportResults.setText("Exported to: " + exportPath + ".gv \n" + 
                exportOutput);
    }
    // --------------------------- --------------------------- ---------------------------



    // --------------------------- --------------------------- ---------------------------
    // PROFILE LOAD METHODS
    // --------------------------- --------------------------- ---------------------------
    
    void loadCharacterProfile1() {     	
        
        // Get name from textbox
        String personName1 = character1.getText();
        String displayPerson1Details = "";

        //Turn map into string, delete curly branckets and eplace commas with new lines
        Map<String, String> characterDetails = 
                getPersonDetails(personName1, "GOT");
        
        for (Map.Entry<String, String> entry : characterDetails.entrySet()) {
            displayPerson1Details = (displayPerson1Details 
                    + entry.getKey() 
                    + ": " 
                    + entry.getValue() 
                    + "\n");
        }
        dispProfile.setText(displayPerson1Details);
        }
        
    
    void loadCharacterProfile2() {     	
        
        // Get name from textbox
        String personName2 = character2.getText();
        String displayPerson2Details = "";

        Map<String, String> characterDetails = 
                getPersonDetails(personName2, "GOT");
        for (Map.Entry<String, String> entry : characterDetails.entrySet()) {
            displayPerson2Details = (displayPerson2Details 
                    + entry.getKey() 
                    + ": " 
                    + entry.getValue() 
                    + "\n");
        }
        dispProfile1.setText(displayPerson2Details);
        }

    // --------------------------- --------------------------- ---------------------------

    @FXML
    void loadDataBlocker(ActionEvent event) {
        URL gotRelations = InterfaceController.class
                .getClassLoader()
                .getResource("GOTRelationships.txt");
        URL gotDetails = InterfaceController.class
                .getClassLoader()
                .getResource("PersonDetails.txt");

        if (gotRelations == null || gotDetails == null) {
            return;
        }

        String gotRelationsPath = new File(gotRelations.getFile())
                .getAbsolutePath();
        String gotDetailsPath = new File(gotDetails.getFile())
                .getAbsolutePath();

        //error if false
        loadRelationsFile(gotRelationsPath, "GOT");
        //error if null
        loadPersonDetailsFile(gotDetailsPath, "GOT");
        loadCharacters("GOT");
        anchorParent.getChildren().remove(loadButton);
    }
    // ----------
    
    //set scene to main scene
    private Scene mainScene;
    
    public void setMainScene(Scene scene) {
        mainScene = scene;
    }
    
    
    //set scene to profile scene
    private Scene profileScene;
    public void setProfileScene(Scene scene) {
    	profileScene = scene;
    }
    
    
    @FXML
    void closeWindow(ActionEvent event) {
    	// go back to main scene when profile / popup closed
    	primaryStage = (Stage)((Node)event.getSource())
                .getScene()
                .getWindow();
        primaryStage.setScene(mainScene);
    }
    @FXML
    void exportSortedList(ActionEvent event) {
        File exportListPath = fileChooser.showSaveDialog(primaryStage);
        if(exportListPath == null){
            return;
            //popup, what is cancel is pressed?
        }
        
        exportSorted(exportListPath.getAbsolutePath(),"GOT");
        //popup confirm exported file
    }   
    
    void loadCharacters(String familyName){
        String[] charactersNames = getAllPeople(familyName); 
        if (charactersNames == null){
            return; 
            //popup?
        }
        
        ObservableList<String> obsNames = FXCollections
                .observableArrayList(charactersNames);
        
        
        characterSelect1.setItems(obsNames);
        characterSelect2.setItems(obsNames);
    }
    
    @FXML public void list1Clicked(MouseEvent arg0) {
        character1.setText(characterSelect1
                .getSelectionModel()
                .getSelectedItem());
        displayRelationship();
        loadCharacterProfile1();
}
    
    @FXML public void list2Clicked(MouseEvent arg0) {
        character2.setText(characterSelect2
                .getSelectionModel()
                .getSelectedItem());
        displayRelationship();
        loadCharacterProfile2();
}

    public void initialize() { 
    }
}