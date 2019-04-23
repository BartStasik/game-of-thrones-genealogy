package com.got.genealogy.userinterface;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

import static com.got.genealogy.core.processor.Genealogy.findRelationship;
import static com.got.genealogy.core.processor.Genealogy.getPersonDetails;


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
    private Label dispField;

    @FXML
    private Button submitBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private ComboBox<?> charComboBox;



    @FXML
    void findRelation(ActionEvent event) {
        String char1 = person1.getText();
        String char2 = person2.getText();

        //Find relation
        String[] relationship = findRelationship(char1, char2, "Stark");
        //System.out.println(relationship);

        for( int i = 0; i < relationship.length - 1; i++)
        {
            String element = relationship[i];
            String nextElement = relationship[i+1];
            dispField.setText(element + " and " + nextElement);
        }


//        String current = relationship[i];
//        if (i != relationship.length - 1) {
//            String next = relationship[i+1];
//            dispField.setText(next);
//        }

//        for(int i = 0; i < relationship.length; i++){
//
//            String element = relationship[i];
//            dispField.setText(element);
//            //add labels[i] to the appropriate Container
//
//        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        person1.clear();
        person2.clear();
        dispField.setText("");

    }

    @FXML
    void viewProfile(ActionEvent event) {
        //String[] profile = getPersonDetails("Jon Snow", "Stark");
    }

    private Scene secondScene;

    @FXML private Text actiontarget;
    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }

    //load the main.fxml screen when "play" is clicked
    @FXML protected void startGame(ActionEvent event) throws Exception  {
    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
    }

    //load the main.fxml screen when "play" is clicked
    private Scene firstScene;
    public void setFirstScene(Scene scene) {
        firstScene = scene;
    }
    public void openFirstScene(ActionEvent actionEvent) {    
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(firstScene);
    }
    
//    private Scene popupScene;
//
//    public void setPopupScene(Scene scene) {
//    	popupScene = scene;
//    }
//
//    @FXML protected void loadResults(ActionEvent event) throws Exception  {
//    	Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        primaryStage.setScene(popupScene);
//    }

    @FXML
    void initialize(URL location, ResourceBundle resources) {
        assert profileBtn != null : "fx:id=\"profileBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert charComboBox != null : "fx:id=\"charComboBox\" was not injected: check your FXML file 'main.fxml'.";
        assert person1 != null : "fx:id=\"person1\" was not injected: check your FXML file 'main.fxml'.";
        assert person2 != null : "fx:id=\"person2\" was not injected: check your FXML file 'main.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert clearBtn != null : "fx:id=\"clearBtn\" was not injected: check your FXML file 'main.fxml'.";
        assert dispField != null : "fx:id=\"dispField\" was not injected: check your FXML file 'main.fxml'.";


    }
}



