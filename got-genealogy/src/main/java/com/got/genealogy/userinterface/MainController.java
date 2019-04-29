package com.got.genealogy.userinterface;

import com.got.genealogy.core.family.FamilyTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import static com.got.genealogy.core.processor.Genealogy.*;
import static com.got.genealogy.core.processor.data.FileHandler.decodeResource;
import static com.got.genealogy.core.processor.data.FileHandler.decodeURL;
import com.got.genealogy.core.processor.data.StringUtils;
import static com.got.genealogy.core.processor.data.StringUtils.toTitleCase;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.FileChooser.ExtensionFilter;


public class MainController {
    
    Stage primaryStage;
    
    Alert error;
    Alert info;

    private FileChooser txtSave, gvSave;

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
        if (character1 == null || character2 == null) {
            return;
        }
        String person1Name = character1.getText();
        String person2Name = character2.getText();

        if (character1.getText().equals("") 
                || character2.getText().equals("")) {
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
        gvSave.getExtensionFilters().addAll(
         new ExtensionFilter("Graphviz", "*.gv"));
        File exportPath = gvSave.showSaveDialog(primaryStage);
        if (exportPath == null) {
            //cancel was pressed
            return;
        }
        String exportPathDecoded = decodeURL(exportPath.getAbsolutePath());
        if (exportPathDecoded == null) {
            error.setContentText("Family tree was not exported!"
                    + "\n Error with path.");
            error.showAndWait();
            return;
        }

        // 2. export file
        exportDOT(exportPathDecoded, "GOT");

        // 3. Display filepath 
        info.setContentText((
                "Family tree successfully exported to: "
                + StringUtils.writeFileExtension(exportPathDecoded, ".gv")));
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
            error.setContentText("Error could not load " 
                    + personName1 
                    + "'s details");
            error.showAndWait();
            return;
        }
        
        String status1 = character1Details.get("LIFE STATUS");
        if (status1 == null) {
            status1 = "None";
        }
        String gender1 = character1Details.get("GENDER");
        if (gender1 == null) {
            gender1 = "None";
        }
        String origin1 = character1Details.get("ORIGIN");
        if (origin1 == null) {
            origin1 = "None";
        }
        String house1 = character1Details.get("HOUSE");
        if (house1 == null) {
            house1 = "None";
        }
        String culture1 = character1Details.get("CULTURE");
        if (culture1 == null) {
            culture1 = "None";
        }
        String alleigance1 = character1Details.get("ALLEGIANCE");
        if (alleigance1 == null) {
            alleigance1 = "None";
        }
        culture1 = culture1.replace(";", " & ")
                .trim();
        origin1 = origin1.replace(";", " & ")
                .trim();
        
        character1Gender.setText(toTitleCase(gender1));
        character1Status.setText(toTitleCase(status1));
        character1Origin.setText(toTitleCase(origin1));
        character1House.setText(toTitleCase(house1));
        character1Culture.setText(toTitleCase(culture1));
        character1Alliance.setText(toTitleCase(alleigance1).replace("; ", "\n"));
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
            error.setContentText("Error could not load " 
                    + personName2 
                    + "'s details");
            error.showAndWait();
            return;
        }
        String status2 = character2Details.get("LIFE STATUS");
        if (status2 == null) {
            status2 = "None";
        }
        String gender2 = character2Details.get("GENDER");
        if (gender2 == null) {
            gender2 = "None";
        }
        String origin2 = character2Details.get("ORIGIN");
        if (origin2 == null) {
            origin2 = "None";
        }
        String house2 = character2Details.get("HOUSE");
        if (house2 == null) {
            house2 = "None";
        }
        String culture2 = character2Details.get("CULTURE");
        if (culture2 == null) {
            culture2 = "None";
        }
        String alleigance2 = character2Details.get("ALLEGIANCE");
        if (alleigance2 == null) {
            alleigance2 = "None";
        }
        culture2 = culture2.replace(";", " & ")
                .trim();
        origin2 = origin2.replace(";", " & ")
                .trim();
        
        character2Gender.setText(toTitleCase(gender2));
        character2Status.setText(toTitleCase(status2));
        character2Origin.setText(toTitleCase(origin2));
        character2House.setText(toTitleCase(house2));
        character2Culture.setText(toTitleCase(culture2));
        character2Alliance.setText(toTitleCase(alleigance2).replace("; ", "\n"));
    }

    // --------------------------- --------------------------- ---------------------------
    @FXML
    void loadDataBlocker(ActionEvent event) {
        InputStream gotRelations = decodeResource("data/GenealogyTree.txt");
        InputStream gotDetails = decodeResource("data/PersonDetails.txt");

        if (gotRelations == null || gotDetails == null) {
            error.setContentText("Could not load the input files!");
            error.showAndWait();
            return;
        }


        FamilyTree realtionsFileLoad = loadRelationsFile(gotRelations, "GOT");
        if(realtionsFileLoad == null){
            error.setContentText("Could not load the Relationships data file from system!");
            error.showAndWait();
        }
        
        info.setContentText("Sucessfully imported relations data file");
        info.showAndWait();

        boolean detailsFileLoad = loadPersonDetailsFile(gotDetails, "GOT");
        if(!detailsFileLoad){
            error.setContentText("Could not load the Characters details file from system!");
            error.showAndWait();
        }
        
        info.setContentText("Sucessfully imported characters details data file");
        info.showAndWait();
        
        loadCharacters("GOT");
        
        anchorParent.getChildren().remove(loadButton);
    }

    @FXML
    void exportSortedList(ActionEvent event) {
        txtSave.getExtensionFilters().addAll(
         new ExtensionFilter("TextFile", "*.txt"));
        File exportListPath = txtSave.showSaveDialog(primaryStage);
        if (exportListPath == null) {
            //cancel pressed
            return;
        }

        String exportPath = decodeURL(exportListPath.getAbsolutePath());
        if (exportPath == null) {
            error.setContentText("Error could not export sorted list!"
                    + "\n Error with path. ");
            error.showAndWait();
            return;
        }

        exportSorted(exportPath, "GOT");
        info.setContentText("Sorted list successfully exported to: " 
                + StringUtils.writeFileExtension(exportPath, ".txt"));
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
        
        gvSave = new FileChooser();
        txtSave = new FileChooser();
        
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
