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

import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.getPersonDetails;


public class FXMLController {

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
    private TextField person1;

    @FXML
    private TextField person2;

    @FXML
    private Button submitBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Label dispField;



    // --------------------------- --------------------------- ---------------------------
    // FIND RELATIONSHIP METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void findRelation(ActionEvent event) {
        String char1 = person1.getText();
        String char2 = person2.getText();

        //Find relationship - method comes from Genealogy
        String[] relationship = findRelationship(char1, char2, "Stark");

        // Print out list of relationship attributes between two characters
        for( int i = 0; i < relationship.length; i++)
        {
            String element = relationship[i];
            dispField.setText(element);
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
    }

    @FXML
    void exportFamily(ActionEvent event) {
        // 2. export file using load method
        // 3. Display filepath and array of items that were exported on screen

    }
    // --------------------------- --------------------------- ---------------------------



    // --------------------------- --------------------------- ---------------------------
    // PROFILE LOAD METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void loadProfile(ActionEvent event) {
        // Name is typed into textbox
        String personName = personProfile.getText();

        // Display name, family and height of person in dispProfile label box
        // code goes here Josh
        
        dispProfile.setText((getPersonDetails(personName,"Stark").toString()).replaceAll(",", "\n"));
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
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
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
    void initialize(URL location, ResourceBundle resources) {
        assert profileBtn != null : "fx:id=\"profileBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert person1 != null : "fx:id=\"person1\" was not injected: check your FXML file 'main.fxml'.";
        assert person2 != null : "fx:id=\"person2\" was not injected: check your FXML file 'main.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert clearBtn != null : "fx:id=\"clearBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert dispField != null : "fx:id=\"dispField\" was not injected: check your FXML file 'main.fxml'.";
    }
}



