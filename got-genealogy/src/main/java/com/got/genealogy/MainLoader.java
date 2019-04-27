package com.got.genealogy;

import com.got.genealogy.userinterface.InterfaceController;

import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;

public class MainLoader extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();

        URL interfaceFXML = getClass()
                .getResource("/fxml/interface.fxml");
        URL mainFXML = getClass()
                .getResource("/fxml/main.fxml");

        if (interfaceFXML == null || mainFXML == null) {
            return;
            //popup
        }

        interfaceLoader.setLocation(interfaceFXML);
        mainLoader.setLocation(mainFXML);

        AnchorPane interfacePane = interfaceLoader.load();
        VBox mainPane = mainLoader.load();

        Scene interfaceScene = new Scene(interfacePane);
        Scene mainScene = new Scene(mainPane);

        InputStream gotIcon = decodeResource("icon.png");

        if (gotIcon == null) {
            return;
            //popup
        }

        primaryStage.getIcons().add(new Image(gotIcon));
        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(interfaceScene);
        primaryStage.show();

        // inject main.fxml scene into the controller of the interface.fxml scene
        InterfaceController interfacePaneController = interfaceLoader.getController();
        interfacePaneController.setMainScene(mainScene);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
