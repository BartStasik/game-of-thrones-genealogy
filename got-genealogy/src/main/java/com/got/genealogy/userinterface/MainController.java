package com.got.genealogy.userinterface;

import com.got.genealogy.core.processor.Genealogy;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.exportDOT;
import java.util.Arrays;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;


public class MainController implements Initializable{
    
    Stage primaryStage;

    String exportPath;
    FileChooser fileChooser = new FileChooser();
    ObservableList characters = FXCollections.observableArrayList();
    
    @FXML 
    private ListView<String> characterSelect1;
    
    @FXML 
    private ListView<String> characterSelect2;
    
    @FXML 
    private Button profileBtn;

    @FXML 
    private Button exportFamily;

    @FXML 
    private TextField personProfile;

    @FXML
    private Label dispProfile;
    
    @FXML
    private Label character1;
        
    @FXML
    private Label character2;

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
    
    
    //profile.fxml elements
    @FXML
    private Label dispNamePopup;

    @FXML
    private Button closeWindow;

    @FXML
    private Label dispProfilePopup;

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

        //Find relationship - method comes from Genealogy
        String[] relationship = findRelationship(person1Name, person2Name, "Stark");
        dispField.setText("");
        
        // Print out list of relationship attributes between two characters
        for (String element : relationship) {
            dispField.setText(dispField.getText() + "\n" + element);
        }
    }


    // --------------------------- --------------------------- ---------------------------
    // EXPORT FILE METHODS
    // --------------------------- --------------------------- ---------------------------


    @FXML
    void exportFamily(ActionEvent event) {
        // 1. File Chooser (showSaveDialog) - return path
        exportPath = fileChooser.showSaveDialog(primaryStage).toString();
        dispExportResults.setWrapText(true);
        dispExportResults.setText("Exporting to: " + exportPath + "\n");
        // 2. export file using load method
        // 3. Display filepath and array of items that were exported on screen
        loadCharacters();
        String exportOutput = Arrays.toString(exportDOT(exportPath,"Stark"));
        dispExportResults.setText("Exported to: " + exportPath + "\n" + exportOutput);
    }
    // --------------------------- --------------------------- ---------------------------



    // --------------------------- --------------------------- ---------------------------
    // PROFILE LOAD METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void loadProfile(ActionEvent event) {
    	
//        // Get name from textbox
//        String personName1 = person1.getText();
//        String personName2 = person2.getText();
//
//        //Turn map into string, delete curly branckets and eplace commas with new lines
//        String displayPerson1Details = getPersonDetails(personName1,"Stark").toString().replaceAll(", ", "\n");
//        displayPerson1Details = displayPerson1Details.replaceAll("\\{", "");
//        displayPerson1Details = displayPerson1Details.replaceAll("\\}", "");
//        dispProfile.setText(displayPerson1Details);
        
        //Load profile sceen to display results
    	primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(profileScene);
        
//        String displayPerson2Details = getPersonDetails(personName2,"Stark").toString().replaceAll(", ", "\n");
//        displayPerson2Details = displayPerson2Details.replaceAll("\\{", "");
//        displayPerson2Details = displayPerson2Details.replaceAll("\\}", "");
//        dispProfile1.setText(displayPerson2Details);
    }

    @FXML
    void clearProfileField(ActionEvent event) {
        personProfile.clear();
        dispProfile.setText("");
    }
    // --------------------------- --------------------------- ---------------------------


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
    	primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(mainScene);
    }
    
    public void loadCharacters(){
        String[] charactersNames = Genealogy.getAllCharacters("Stark"); 
        characterSelect1.getItems().addAll(charactersNames);
        characterSelect2.getItems().addAll(charactersNames);
    }
    
    @FXML public void list1Clicked(MouseEvent arg0) {
        character1.setText(characterSelect1.getSelectionModel().getSelectedItem());
        displayRelationship();
}
    
    @FXML public void list2Clicked(MouseEvent arg0) {
        character2.setText(characterSelect2.getSelectionModel().getSelectedItem());
        displayRelationship();
}


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) { 
    }
}