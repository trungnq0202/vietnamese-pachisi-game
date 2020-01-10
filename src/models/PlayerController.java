package models;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PlayerController {
    @FXML private Button createNewPlayerButton;
    @FXML private TextField nameField;

    public void initialize(){
        System.out.println("player controller init");
        setCreateNewPlayerButtonOnAction();
    }

    public void setCreateNewPlayerButtonOnAction() {
        createNewPlayerButton.setOnMouseClicked(e -> {
            if (nameField.getText() != null) {
                System.out.println("hello");
                Player player = new Player();
                player.setPlayerName(nameField.getText());
                System.out.println(player.getPlayerName());
            } else {
            }
        });
    }

}
