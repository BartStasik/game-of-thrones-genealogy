package com.got.genealogy.userinterface;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
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
    
    private Scene popupScene;

    public void setPopupScene(Scene scene) {
    	popupScene = scene;
    }
    
    @FXML protected void loadResults(ActionEvent event) throws Exception  {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(popupScene);
    	System.out.println("Working 2");
    }

}



