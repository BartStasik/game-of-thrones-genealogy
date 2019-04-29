package com.got.genealogy.userinterface;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;


public class InterfaceController {

    Stage primaryStage;

    FileChooser fileChooser;
    String pathMusic;
    Media media;
    MediaPlayer player;
    URL musicFile;

    int toggleAudio;

    @FXML
    private Button volumeButton;

    //set scene to main scene
    private Scene mainScene;

    public void setMainScene(Scene scene) {
        mainScene = scene;
    }

    @FXML
    protected void startGame(ActionEvent event) throws Exception {
        // load the main scene when "play" is clicked
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(mainScene);
    }

    @FXML
    protected void exitGame(ActionEvent event) throws Exception {
        Platform.exit();
    }

    @FXML
    protected void muteMusic(ActionEvent event) throws Exception {
        muteMusicAction();
    }

    public void muteMusicAction() {
        toggleAudio++;
        if ((toggleAudio & 1) == 0) {
            volumeButton
                    .setStyle("-fx-background-image: url('images/volume-mute.png')");
            player.setVolume(0.0);
        } else {
            volumeButton
                    .setStyle("-fx-background-image: url('images/volume-high.png')");
            player.setVolume(0.2);
        }
    }

    @FXML
    public void minimizeClicked(ActionEvent arg0) {
        primaryStage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();
        primaryStage.setIconified(true);
    }
    
    @FXML
    public void closeClicked(ActionEvent arg0) {
        Platform.exit();
    }

    public void initialize() {
        Alert musicAlert = new Alert(AlertType.ERROR);
        musicAlert.setTitle("Error");
        musicAlert.setHeaderText(null);
        musicAlert.setContentText("Failed to load music!");

        
        try {
            musicFile = getClass()
                    .getResource("/music/MainTheme.mp3");
            if (musicFile == null) {
                musicAlert.showAndWait();
                return;
            }

            pathMusic = musicFile.toURI().toString();

            toggleAudio = 1;
            fileChooser = new FileChooser();

        } catch (URISyntaxException e) {
            musicAlert.showAndWait();
        }
        media = new Media(pathMusic);
        player = new MediaPlayer(media);
        player.play();
        player.setVolume(0.2);
        
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }
}