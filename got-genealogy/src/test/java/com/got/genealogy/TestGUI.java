package com.got.genealogy;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestGUI {

    @Test
    void basicGUITest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    new MainLoader().start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("FXML files failed to load!");
                }

            });
        });
        thread.start();
        Thread.sleep(1000);
    }
}
