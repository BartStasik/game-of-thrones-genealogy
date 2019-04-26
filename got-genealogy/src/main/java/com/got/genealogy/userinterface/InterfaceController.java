package com.got.genealogy.userinterface;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import java.io.File;
import java.net.URL;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;


public class InterfaceController{

    
    Stage primaryStage;
    
    String ProfileFilePath;
    String RelationshipFilePath;
    String exportPath;
    FileChooser fileChooser = new FileChooser();
    
    String pathMusic = getClass().getResource("/MainTheme.mp3").toString();
    Media media = new Media(pathMusic);
    MediaPlayer player = new MediaPlayer(media); 
    
    int toggleAudio;
    
    @FXML
    private Button volumeButton;
    
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
    protected void startGame(ActionEvent event) throws Exception  {
        // load the main scene when "play" is clicked
    	primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(mainScene);
    }
    
    @FXML 
    protected void exitGame(ActionEvent event) throws Exception  {
    	Platform.exit();
    }
    
    @FXML 
    protected void muteMusic(ActionEvent event) throws Exception  {
        muteMusicAction();
    }
    
    public void muteMusicAction(){
        toggleAudio++;
        if ( (toggleAudio & 1) == 0 ){
            player.setVolume(0.0);
            volumeButton.setStyle("-fx-background-image: url('volume-mute.png')");
        }
        else {
            player.setVolume(0.3);
            volumeButton.setStyle("-fx-background-image: url('volume-high.png')");
        }
    }
    
    
    public void initialize() { 
       player.play();
       player.setVolume(0.2);
       player.setOnEndOfMedia(new Runnable() {
       public void run() {
         player.seek(Duration.ZERO);
       }
   });
    }
}