package com.got.genealogy.userinterface;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
 
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;

import javafx.scene.Scene;

import javafx.scene.text.Text;
import javafx.stage.Stage;
	 
public class FXMLController {
	
//	private static HashMap<String, Pane> screenMap = new HashMap<>();
//	private static Scene main;
//	
//	
//	public FXMLController(Scene main) {
//        FXMLController.main = main;
//    }
//
//    public static void addScreen(String name, Pane pane){
//         screenMap.put(name, pane);
//    }
//
//    public void removeScreen(String name){
//        screenMap.remove(name);
//    }
//
//    public static void activate(String name){
//        main.setRoot( screenMap.get(name) );
//    }
	
    
    private Scene secondScene;

    @FXML private Text actiontarget;
    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }
    @FXML protected void startGame(ActionEvent event) throws Exception  {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
    	System.out.println("Working");
    }

    private Scene firstScene;

    public void setFirstScene(Scene scene) {
        firstScene = scene;
    }

    public void openFirstScene(ActionEvent actionEvent) {    
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(firstScene);
    }
    

}



