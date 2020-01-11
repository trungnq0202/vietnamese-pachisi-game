package models;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class PlayerController {
    @FXML private Button createNewPlayerButton;
    @FXML private Button createNewClient;
    @FXML private TextField nameField;

    public void initialize(){
        System.out.println("player controller init");
    }



}
