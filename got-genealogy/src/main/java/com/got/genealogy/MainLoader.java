package com.got.genealogy;

import com.got.genealogy.userinterface.FXMLController;
//import com.got.genealogy.userinterface.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLoader extends Application {

   
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();

        interfaceLoader.setLocation(getClass().getResource("/fxml/interface.fxml"));
        mainLoader.setLocation(getClass().getResource("/fxml/main.fxml"));

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/interface.fxml"));
//        Parent secondPane = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        
//        Scene scene = new Scene(root);
//        Scene secondScene = new Scene(secondPane);


        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();

        Scene firstScene = new Scene(interfacePane);
        Scene secondScene = new Scene(mainPane);
//        Scene scene2 = new Scene(mainPane);

        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(firstScene);
        primaryStage.show();
        
        // injecting second scene into the controller of the first scene
        FXMLController firstPaneController = (FXMLController) interfaceLoader.getController();
        firstPaneController.setSecondScene(secondScene);
        
        // injecting first scene into the controller of the second scene
        FXMLController secondPaneController = (FXMLController) mainLoader.getController();
        //secondPaneController.setFirstScene(firstScene);
        
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}