package com.got.genealogy.userinterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.application.Platform;

import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.getPersonDetails;
import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import static com.got.genealogy.core.processor.Genealogy.exportDOT;
import java.io.File;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;


public class FXMLController implements Initializable{
    Stage primaryStage;
    
    String ProfileFilePath;
    String RelationshipFilePath;
    String exportPath;
    FileChooser fileChooser = new FileChooser();
    
    ListView<String> Characterlist = new ListView<String>();
    ObservableList<String> characters = FXCollections.observableArrayList (
    "Single", "Double", "Suite", "Family App");
    
    @FXML
    private Button profileBtn;

    @FXML
    private Button exportFamily;

    @FXML
    private TextField personProfile;

    @FXML
    private Label dispProfile;

    @FXML
    private Button clearBtn2;

    @FXML
    private Label dispExportResults;

    @FXML
    private Button browseBtn;

    @FXML
    private Label dispProfile11;
    
    @FXML
    private Label dispProfile1;

    @FXML
    private TextField person1;

    @FXML
    private TextField person2;

    @FXML
    private Button submitBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Label dispField;
    
    @FXML
    private Label relationshipFileURL;
    
    @FXML
    private Label profileFileURL;
    
    @FXML
    private ListView characterSelect1;
    
    @FXML
    private ListView characterSelect2;

    // --------------------------- --------------------------- ---------------------------
    // FIND RELATIONSHIP METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void findRelation(ActionEvent event) {
        String person1Name = person1.getText();
        String person2Name = person2.getText();

        //Find relationship - method comes from Genealogy
        String[] relationship = findRelationship(person1Name, person2Name, "Stark");
        dispField.setText("");
        
        // Print out list of relationship attributes between two characters
        for (String element : relationship) {
            dispField.setText(dispField.getText() + "\n" + element);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        person1.clear();
        person2.clear();
        dispField.setText("");
    }
    // --------------------------- --------------------------- ---------------------------


    // --------------------------- --------------------------- ---------------------------
    // EXPORT FILE METHODS
    // --------------------------- --------------------------- ---------------------------

    @FXML
    void chooseSaveLocation(ActionEvent event) {
        // 1. File Chooser (showSaveDialog) - return path
        File exportPath = fileChooser.showSaveDialog(primaryStage);
        dispExportResults.setWrapText(true);
        dispExportResults.setText("Exporting to: " + exportPath + "\n");
    }

    @FXML
    void exportFamily(ActionEvent event) {
        // 2. export file using load method
        // 3. Display filepath and array of items that were exported on screen
        String exportOutput = Arrays.toString(exportDOT(exportPath,"Stark"));
        dispExportResults.setText("Exported to: " + exportPath + "\n" + exportOutput);

    }
    // --------------------------- --------------------------- ---------------------------



    // --------------------------- --------------------------- ---------------------------
    // PROFILE LOAD METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void loadProfile(ActionEvent event) {
        // Get name from textbox
        String personName1 = person1.getText();
        String personName2 = person2.getText();

        //Turn map into string, delete curly branckets and eplace commas with new lines
        String displayPerson1Details = getPersonDetails(personName1,"Stark").toString().replaceAll(", ", "\n");
        displayPerson1Details = displayPerson1Details.replaceAll("\\{", "");
        displayPerson1Details = displayPerson1Details.replaceAll("\\}", "");
        dispProfile.setText(displayPerson1Details);
        
        String displayPerson2Details = getPersonDetails(personName2,"Stark").toString().replaceAll(", ", "\n");
        displayPerson2Details = displayPerson2Details.replaceAll("\\{", "");
        displayPerson2Details = displayPerson2Details.replaceAll("\\}", "");
        dispProfile1.setText(displayPerson2Details);
    }

    @FXML
    void clearProfileField(ActionEvent event) {
        personProfile.clear();
        dispProfile.setText("");
    }
    // --------------------------- --------------------------- ---------------------------



    //load the main.fxml screen when "play" is clicked
    private Scene secondScene;
    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }
    @FXML protected void startGame(ActionEvent event) throws Exception  {
        loadPersonDetailsFile(ProfileFilePath,"Stark");
        loadRelationsFile(RelationshipFilePath,"Stark");
    	primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
        
        characterSelect1.setItems(characters);
        characterSelect2.setItems(characters);
    }
    
    @FXML protected void exitGame(ActionEvent event) throws Exception  {
    	Platform.exit();
    }



    // --------------------------- --------------------------- ---------------------------
    // DON'T WORRY ABOUT THESE - FOR NOW WE ARE LOADING ALL RESULTS ON THE main.fxml SCREEN
    // --------------------------- --------------------------- ---------------------------
//    load the interface.fxml screen if there will be a home button
//    private Scene firstScene;
//    public void setFirstScene(Scene scene) {
//        firstScene = scene;
//    }
//    public void openFirstScene(ActionEvent actionEvent) {
//        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        primaryStage.setScene(firstScene);
//    }

    //Load the popup.fxml scene if needed to display results but maybe only when we have time as for now it displays on the main.fxml screen
//    private Scene popupScene;
//
//    public void setPopupScene(Scene scene) {
//    	popupScene = scene;
//    }
//
//    @FXML protected void loadResults(ActionEvent event) throws Exception  {
//    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        primaryStage.setScene(popupScene);
//    }
    // --------------------------- --------------------------- ---------------------------
            
        
    @FXML 
    void loadProfilesFile(ActionEvent event) {
        fileChooser.setTitle("Open Persons Details File");
        ProfileFilePath = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
        profileFileURL.setText(ProfileFilePath);
    }
    
    @FXML 
    void loadRelationshipFile(ActionEvent event) {
        fileChooser.setTitle("Open Relations File");
        RelationshipFilePath = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
        relationshipFileURL.setText(RelationshipFilePath);
    }


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {      
        assert profileBtn != null : "fx:id=\"profileBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert person1 != null : "fx:id=\"person1\" was not injected: check your FXML file 'main.fxml'.";
        assert person2 != null : "fx:id=\"person2\" was not injected: check your FXML file 'main.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert clearBtn != null : "fx:id=\"clearBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert dispField != null : "fx:id=\"dispField\" was not injected: check your FXML file 'main.fxml'.";
    }
}



