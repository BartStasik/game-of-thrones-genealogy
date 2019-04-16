
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
 
public class InterfaceController {
    @FXML
    private Text character;

    @FXML
    private Button closeWindow;

    @FXML
    private Text house;

    @FXML
    private Text gender;

    @FXML
    private Text lifeStatus;

    @FXML
    private Text dob;
    
    @FXML
    private Text characterOne;

    @FXML
    private Text characterTwo;

    @FXML
    private Text actionWord;

    @FXML
    private Button closeWindow1;
    
    
    
    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        /*Window owner = submitButton.getScene().getWindow();x
        if(nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", 
                    "Please enter your name");
            return;
        }
        if(emailField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", 
                    "Please enter your email id");
            return;
        }
        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", 
                    "Please enter a password");
            return;
        }

        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!", 
                "Welcome " + nameField.getText());*/
    }

}