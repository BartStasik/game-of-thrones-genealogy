package com.got.genealogy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();

        interfaceLoader.setLocation(getClass().getResource("/fxml/interface.fxml"));
        mainLoader.setLocation(getClass().getResource("/fxml/main.fxml"));



        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();

        Scene scene = new Scene(interfacePane);
        //Scene scene = new Scene(mainPane);

        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}