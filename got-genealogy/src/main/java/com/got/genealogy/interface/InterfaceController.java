import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.application.Application;

public class InterfaceController extends AnchorPane {

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView daenerysTargaryen;

    @FXML
    private ImageView johnSnow;

    @FXML
    private ImageView cerseiLanister;

    @FXML
    private ImageView nedStark;

    @FXML
    private Button sort;

    @FXML
    void initialize() {
        assert daenerysTargaryen != null : "fx:id=\"daenerysTargaryen\" was not injected: check your FXML file 'characters.fxml'.";
        assert johnSnow != null : "fx:id=\"johnSnow\" was not injected: check your FXML file 'characters.fxml'.";
        assert cerseiLanister != null : "fx:id=\"cerseiLanister\" was not injected: check your FXML file 'characters.fxml'.";
        assert nedStark != null : "fx:id=\"nedStark\" was not injected: check your FXML file 'characters.fxml'.";
        assert sort != null : "fx:id=\"sort\" was not injected: check your FXML file 'characters.fxml'.";

    }
    
    //Constructor
    public InterfaceController {

    	//FXML Loader
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../../../res/characters.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
       
    	try {
    		loader.load();
    	} catch (IOException ex) {
    		Logger.getlogger(getClass().getName()).log(Level.SEVER, " FXML can't be loaded!", ex);
    	}

    }

    private void initialize() {
    	
    	//start 
    	start.setOnAction(a -> {
    		statusLabel.setText("Status : [Running]");
    		
    		//method
    		
    	}
    }
    
}





//public static void main(String[] args) { launch(args); }