package controllers;

import javafx.fxml.FXML;

public class MainController {
    @FXML private HorseController horseController;

    public MainController(){

    }

    @FXML private void initialize(){
        horseController.injectMainController(this);
    }


}
