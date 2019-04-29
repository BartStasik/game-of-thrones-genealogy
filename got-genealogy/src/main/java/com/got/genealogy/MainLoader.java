package com.got.genealogy;

import com.got.genealogy.userinterface.InterfaceController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class MainLoader extends Application {
    
    Alert error;
    
    private double xOffset = 0;
    private double yOffset = 0;


    @Override
    public void start(Stage primaryStage) throws IOException {
        error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        
        URL fontURL = getClass()
                .getResource("/fonts/Cinzel.ttf");
        
        if (fontURL == null) {
            error.setContentText("Could not set font from URL: " + fontURL);
            error.showAndWait();
            return;
        }
        
        Font.loadFont(fontURL.toExternalForm(), 10);
  
        FXMLLoader interfaceLoader = new FXMLLoader();
        FXMLLoader mainLoader = new FXMLLoader();

        URL interfaceFXML = getClass()
                .getResource("/fxml/interface.fxml");
        URL mainFXML = getClass()
                .getResource("/fxml/main.fxml");

        if (interfaceFXML == null || mainFXML == null) {
            error.setContentText("Could not load GUI FXML files");
            error.showAndWait();
            return;
        }

        interfaceLoader.setLocation(interfaceFXML);
        mainLoader.setLocation(mainFXML);

        AnchorPane interfacePane = interfaceLoader.load();
        AnchorPane mainPane = mainLoader.load();

        Scene interfaceScene = new Scene(interfacePane);
        Scene mainScene = new Scene(mainPane);
        
        Image cursorImage = new Image(getClass()
                .getResource("/images/cursor.png").toExternalForm());
        
        if (cursorImage == null) {
            error.setContentText("Could not load custom cursor from " 
                    + cursorImage);
            error.showAndWait();
            return;
        }
        
        interfaceScene.setCursor(new ImageCursor(cursorImage));
        mainScene.setCursor(new ImageCursor(cursorImage));

        InputStream gotIcon = decodeResource("images/icon.png");

        if (gotIcon == null) {
            error.setContentText("Could not load application icon");
            error.showAndWait();
            return;
        }

        primaryStage.getIcons().add(new Image(gotIcon));
        primaryStage.setTitle("Game Of Thrones Genealogy");
        primaryStage.setScene(interfaceScene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        // inject main.fxml scene into the controller of the interface.fxml scene
        InterfaceController interfacePaneController = interfaceLoader.getController();
        interfacePaneController.setMainScene(mainScene);
        
        interfaceScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        
        //move around here
        interfaceScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        
        mainScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        
        //move around here
         mainScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
