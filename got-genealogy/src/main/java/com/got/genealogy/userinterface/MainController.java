package com.got.genealogy.userinterface;

import com.got.genealogy.core.family.FamilyTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import static com.got.genealogy.core.processor.Genealogy.*;
import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;
import static com.got.genealogy.core.processor.data.FileHandler.decodeURL;
import javafx.application.Platform;
import javafx.scene.Node;


public class MainController {
    
    Stage primaryStage;
    
    Alert error;
    Alert info;

    private FileChooser fileChooser;

    @FXML
    private Button loadButton;

    @FXML
    private AnchorPane anchorParent;

    @FXML
    private ListView<String> characterSelect1;

    @FXML
    private ListView<String> characterSelect2;

    @FXML
    private Label character1Alliance;

    @FXML
    private Label character1Culture;

    @FXML
    private Label character1Status;

    @FXML
    private Label character1Gender;

    @FXML
    private Label character1Origin;

    @FXML
    private Label character1House;

    @FXML
    private Label character2Alliance;

    @FXML
    private Label character2Culture;

    @FXML
    private Label character2Status;

    @FXML
    private Label character2Gender;

    @FXML
    private Label character2Origin;

    @FXML
    private Label character2House;

    @FXML
    private Label character1;

    @FXML
    private Label character2;

    @FXML
    private ImageView allianceIcon1;

    @FXML
    private ImageView cultureIcon1;

    @FXML
    private ImageView statusIcon1;

    @FXML
    private ImageView genderIcon1;

    @FXML
    private ImageView originIcon1;

    @FXML
    private ImageView houseIcon1;

    @FXML
    private ImageView allianceIcon2;

    @FXML
    private ImageView cultureIcon2;

    @FXML
    private ImageView statusIcon2;

    @FXML
    private ImageView genderIcon2;

    @FXML
    private ImageView originIcon2;

    @FXML
    private ImageView houseIcon2;

    @FXML
    private Label dispField;

    // --------------------------- --------------------------- ---------------------------
    // FIND RELATIONSHIP METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void findRelation(ActionEvent event) {
        displayRelationship();
    }

    void displayRelationship() {
        String person1Name = character1.getText();
        String person2Name = character2.getText();

        if (character1.getText().equals("") || character2.getText().equals("")) {
            //Both characters have not been selected there is no relationship to display
            return;
        } else if (character1.getText().equals(character2.getText())) {
            //Character 1 and 2 are the same person
            dispField.setText("Is");
        } else {
            //Find relationship - method comes from Genealogy
            String[] relationship
                    = findRelationship(person1Name, person2Name, "GOT");
            if (relationship == null) {
                return;
            }
            dispField.setText("");

            // Print out list of relationship attributes between two characters
            for (String realtionshipType : relationship) {
                dispField.setText(
                        dispField.getText()
                        + realtionshipType
                        + "\n");
            }
        }
    }

    // --------------------------- --------------------------- ---------------------------
    // EXPORT FILE METHODS
    // --------------------------- --------------------------- ---------------------------
    @FXML
    void exportFamily(ActionEvent event) {
        // 1. File Chooser (showSaveDialog) - return path
        File exportPath = fileChooser.showSaveDialog(primaryStage);
        if (exportPath == null) {
            //cancel was pressed
            return;
        }
        String exportPathDecoded = decodeURL(exportPath.getAbsolutePath());
        if (exportPathDecoded == null) {
            error.setContentText("Family tree was not exported!\n Error with path.");
            error.showAndWait();
            return;
        }

        // 2. export file
        exportDOT(exportPathDecoded, "GOT");

        // 3. Display filepath 
        info.setContentText((
                "Family tree successfully exported to: "
                + exportPathDecoded
                + ".gv"));
        info.showAndWait();
    }

    // --------------------------- --------------------------- ---------------------------
    // PROFILE LOAD METHODS
    // --------------------------- --------------------------- ---------------------------
    void loadCharacterProfile1() {

        // Get name from textbox
        String personName1 = character1.getText();

        if (personName1.length() == 0) {
            return;
        }
        //Turn map into string, delete curly branckets and eplace commas with new lines
        Map<String, String> character1Details
                = getPersonDetails(personName1, "GOT");

        if (character1Details == null) {
            error.setContentText("Error could not load " + personName1 + "'s details");
            error.showAndWait();
            return;
        }

        character1Gender.setText(character1Details.get("GENDER"));
        character1Status.setText(character1Details.get("LIFE STATUS"));
        character1Origin.setText(character1Details.get("ORIGIN"));
        character1House.setText(character1Details.get("HOUSE"));
        character1Culture.setText(character1Details.get("CULTURE"));
        character1Alliance.setText(character1Details.get("ALLEGIANCE").replace("; ", "\n"));
    }

    void loadCharacterProfile2() {

        // Get name from textbox
        String personName2 = character2.getText();

        if (personName2.length() == 0) {
            return;
        }
        Map<String, String> character2Details
                = getPersonDetails(personName2, "GOT");

        if (character2Details == null) {
            error.setContentText("Error could not load " + personName2 + "'s details");
            error.showAndWait();
            return;
        }

        character2Gender.setText(character2Details.get("GENDER"));
        character2Status.setText(character2Details.get("LIFE STATUS"));
        character2Origin.setText(character2Details.get("ORIGIN"));
        character2House.setText(character2Details.get("HOUSE"));
        character2Culture.setText(character2Details.get("CULTURE"));
        character2Alliance.setText(character2Details.get("ALLEGIANCE").replace("; ", "\n"));
    }

    // --------------------------- --------------------------- ---------------------------
    @FXML
    void loadDataBlocker(ActionEvent event) {
        loadButton.setStyle("-fx-background-image: url('button-bg-cracked.jpg');");
        InputStream gotRelations = decodeResource("GenealogyTree.txt");
        InputStream gotDetails = decodeResource("PersonDetails.txt");

        if (gotRelations == null || gotDetails == null) {
            error.setContentText("Could not load the input files!");
            error.showAndWait();
            return;
        }


        FamilyTree realtionsFileLoad = loadRelationsFile(gotRelations, "GOT");
        if(realtionsFileLoad == null){
            error.setContentText("Could not load the Relationships data file fropm system!");
            error.showAndWait();
        }

        boolean detailsFileLoad = loadPersonDetailsFile(gotDetails, "GOT");
        if(!detailsFileLoad){
            error.setContentText("Could not load the Characters details file fropm system!");
            error.showAndWait();
        }
        
        loadCharacters("GOT");
        
        //pause to show ice breaking
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        anchorParent.getChildren().remove(loadButton);
    }

    @FXML
    void exportSortedList(ActionEvent event) {
        File exportListPath = fileChooser.showSaveDialog(primaryStage);
        if (exportListPath == null) {
            //cancel pressed
            return;
        }

        String exportPath = decodeURL(exportListPath.getAbsolutePath());
        if (exportPath == null) {
            error.setContentText("Error could not export sorted list!\n Error with path. ");
            error.showAndWait();
            return;
        }

        exportSorted(exportPath, "GOT");
        info.setContentText("Sorted list successfully exported to: " + exportPath);
        info.showAndWait();
    }

    void loadCharacters(String familyName) {
        String[] charactersNames = getAllPeople(familyName);
        if (charactersNames == null) {
            error.setContentText("Could not load the Characters names!");
            error.showAndWait();
            return;
        }

        ObservableList<String> obsNames = FXCollections
                .observableArrayList(charactersNames);

        characterSelect1.setItems(obsNames);
        characterSelect2.setItems(obsNames);
    }

    @FXML
    public void list1Clicked(MouseEvent arg0) {
        character1.setText(characterSelect1
                .getSelectionModel()
                .getSelectedItem());
        displayRelationship();
        loadCharacterProfile1();
    }

    @FXML
    public void list2Clicked(MouseEvent arg0) {
        character2.setText(characterSelect2
                .getSelectionModel()
                .getSelectedItem());
        displayRelationship();
        loadCharacterProfile2();
    }
    
    @FXML
    public void minimizeClicked(ActionEvent arg0) {
        primaryStage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();
        primaryStage.setIconified(true);
    }
    
    @FXML
    public void closeClicked(ActionEvent arg0) {
        Platform.exit();
    }

    public void initialize() {
        error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        
        info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Information");
        info.setHeaderText(null);
        
        fileChooser = new FileChooser();
        
        Tooltip.install(allianceIcon1, new Tooltip("Alleigence"));
        Tooltip.install(cultureIcon1, new Tooltip("Culture"));
        Tooltip.install(statusIcon1, new Tooltip("Life Status"));
        Tooltip.install(genderIcon1, new Tooltip("Gender"));
        Tooltip.install(originIcon1, new Tooltip("Origin"));
        Tooltip.install(houseIcon1, new Tooltip("House"));
        Tooltip.install(allianceIcon2, new Tooltip("Alleigence"));
        Tooltip.install(cultureIcon2, new Tooltip("Culture"));
        Tooltip.install(statusIcon2, new Tooltip("Life Status"));
        Tooltip.install(genderIcon2, new Tooltip("Gender"));
        Tooltip.install(originIcon2, new Tooltip("Origin"));
        Tooltip.install(houseIcon2, new Tooltip("House"));
    }
}
