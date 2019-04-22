package com.got.genealogy.userinterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
	 
public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField person1;

    @FXML
    private TextField person2;

    @FXML
    private TextField dispField;

    @FXML
    private Button submitBtn;

    @FXML
    private Button submitHistory;

    @FXML
    void findRelation(ActionEvent event) {
        String char1 = person1.getText();
        String char2 = person2.getText();
        findRelationship(char1, char2);

        dispField.setText(char1);
    }

    private Scene secondScene;

    @FXML private Text actiontarget;
    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }
    @FXML protected void startGame(ActionEvent event) throws Exception  {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
//    	System.out.println("Working");
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

    @FXML
    void initialize() {
        assert person1 != null : "fx:id=\"person1\" was not injected: check your FXML file 'main.fxml'.";
        assert person2 != null : "fx:id=\"person2\" was not injected: check your FXML file 'main.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert submitHistory != null : "fx:id=\"submitHistory\" was not injected: check your FXML file 'main.fxml'.";

    }
}



