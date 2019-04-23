package com.got.genealogy;

import com.got.genealogy.userinterface.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.got.genealogy.core.processor.Genealogy.loadRelation;
import static com.got.genealogy.core.processor.Genealogy.loadPersonDetails;

public class MainLoader extends Application {

   
    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();
        FXMLLoader popupLoader = new FXMLLoader();

        interfaceLoader.setLocation(getClass().getResource("/fxml/interface.fxml"));
        mainLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        popupLoader.setLocation(getClass().getResource("/fxml/popup.fxml"));

        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();
        AnchorPane popupPane = popupLoader.load();

        Scene firstScene = new Scene(interfacePane);
        Scene secondScene = new Scene(mainPane);
        Scene popupScene = new Scene(popupPane);

        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(firstScene);
        primaryStage.show();
        
        // inject second scene into the controller of the first scene
        FXMLController firstPaneController = (FXMLController) interfaceLoader.getController();
        firstPaneController.setSecondScene(secondScene);
        
        // inject first scene into the controller of the main scene
        FXMLController secondPaneController = (FXMLController) mainLoader.getController();
        secondPaneController.setFirstScene(firstScene);
        
//        FXMLController popupPaneController = (FXMLController) popupLoader.getController();
//        popupPaneController.setPopupScene(popupScene);


        loadRelation("/Users/ashmac/GitHub/game-of-thrones-genealogy/got-genealogy/src/main/resources/InputFile.txt", "Stark");
        loadPersonDetails("/Users/ashmac/GitHub/game-of-thrones-genealogy/got-genealogy/src/main/resources/PersonDetailsTestFile.txt", "Stark");
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}