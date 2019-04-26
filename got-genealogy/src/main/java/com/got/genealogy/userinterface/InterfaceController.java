package com.got.genealogy.userinterface;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;


public class InterfaceController {

    
    Stage primaryStage;
    
    String ProfileFilePath;
    String RelationshipFilePath;
    String exportPath;
    FileChooser fileChooser = new FileChooser();
    
    @FXML
    private Label relationshipFileURL;
    
    @FXML
    private Label profileFileURL;

    //set scene to main scene
    private Scene mainScene;
    
    public void setMainScene(Scene scene) {
        mainScene = scene;
    }
    
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
    protected void startGame(ActionEvent event) throws Exception  {
        loadPersonDetailsFile(ProfileFilePath,"Stark");
        loadRelationsFile(RelationshipFilePath,"Stark");
        
        // load the main scene when "play" is clicked
    	primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(mainScene);   
    }
    
    @FXML 
    protected void exitGame(ActionEvent event) throws Exception  {
    	Platform.exit();
    }
    
    public void initialize(URL location, ResourceBundle resources) { 
    }
}