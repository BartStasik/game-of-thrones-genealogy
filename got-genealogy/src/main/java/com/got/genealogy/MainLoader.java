package com.got.genealogy;

import com.got.genealogy.userinterface.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.got.genealogy.core.processor.Genealogy.loadRelationsFile;
import static com.got.genealogy.core.processor.Genealogy.loadPersonDetailsFile;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

public class MainLoader extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();
        FXMLLoader popupLoader = new FXMLLoader();

        interfaceLoader.setLocation(getClass().getResource("/fxml/interface.fxml"));
        mainLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        //popupLoader.setLocation(getClass().getResource("/fxml/popup.fxml"));

        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();
        //AnchorPane popupPane = popupLoader.load();

        Scene firstScene = new Scene(interfacePane);
        Scene secondScene = new Scene(mainPane);
        //Scene popupScene = new Scene(popupPane);

        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(firstScene);
        primaryStage.show();
        
        // inject main.fxml scene into the controller of the interface.fxml scene
        FXMLController firstPaneController = (FXMLController) interfaceLoader.getController();
        firstPaneController.setSecondScene(secondScene);
        
        // inject first scene into the controller of the main.fxml scene
        //FXMLController secondPaneController = (FXMLController) mainLoader.getController();
        //secondPaneController.setFirstScene(firstScene);
        
        //FXMLController popupPaneController = (FXMLController) popupLoader.getController();
        //popupPaneController.setPopupScene(popupScene);
        
        
        //TemporaryDialogs for opening relation and details files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Relations File");
        loadRelationsFile(FileUtils.readFileToString(fileChooser.showOpenDialog(primaryStage)),"Stark");
        
        fileChooser.setTitle("Open Relations File");
        loadPersonDetailsFile(FileUtils.readFileToString(fileChooser.showOpenDialog(primaryStage)),"Stark");
        
        //Get the test files from resources in order for load methods etc in FXMLController to work
        //loadRelationsFile("/Users/ashmac/GitHub/game-of-thrones-genealogy/got-genealogy/src/main/resources/InputFile.txt", "Stark");
        //loadPersonDetailsFile("/Users/ashmac/GitHub/game-of-thrones-genealogy/got-genealogy/src/main/resources/PersonDetailsTestFile.txt", "Stark");
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}