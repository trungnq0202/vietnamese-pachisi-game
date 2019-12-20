package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainController {
    @FXML private MenuController menuController;

    @FXML private void initialize(){
        injectMainControllerInNestedControllers();

    }

    private void injectMainControllerInNestedControllers(){
        getMenuController().injectMainController(this);
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }
}
