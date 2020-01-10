package models;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import networking.Client;
import networking.Server;

public class PlayerController {
    @FXML private Button createNewPlayerButton;
    @FXML private Button createNewClient;
    @FXML private TextField nameField;

    public void initialize(){
        System.out.println("player controller init");
    }



}
