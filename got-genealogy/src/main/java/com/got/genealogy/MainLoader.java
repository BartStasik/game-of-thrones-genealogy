package com.got.genealogy;

import com.got.genealogy.userinterface.MainController;
import com.got.genealogy.userinterface.InterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLoader extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


         FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();
        FXMLLoader profileLoader = new FXMLLoader();

        interfaceLoader.setLocation(getClass().getResource("/fxml/interface.fxml"));
        mainLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        profileLoader.setLocation(getClass().getResource("/fxml/profile.fxml"));

        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();
        AnchorPane profilePane = profileLoader.load();

        Scene interfaceScene = new Scene(interfacePane);
        Scene mainScene = new Scene(mainPane);
        Scene profileScene = new Scene(profilePane);
        
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(interfaceScene);
        primaryStage.show();
        
        // inject main.fxml scene into the controller of the interface.fxml scene
        InterfaceController interfacePaneController = (InterfaceController) interfaceLoader.getController();
        interfacePaneController.setMainScene(mainScene);
        
        // inject popup.fxml scene into the controller of the main.fxml scene
        MainController mainPaneController = (MainController) mainLoader.getController();
        mainPaneController.setProfileScene(profileScene);
        
        // inject main.fxml scene into the controller of the popup.fxml scene
        MainController profilePaneController = (MainController) profileLoader.getController();
        profilePaneController.setMainScene(mainScene);

    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
