package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class MenuController {
    @FXML private AnchorPane menu;
    @FXML private Circle c1;
    @FXML private Circle c2;
    @FXML private Circle c3;
    @FXML private Circle c4;
    @FXML private Circle c5;
    @FXML private Circle c6;
    @FXML private Circle c7;
    @FXML private Circle c8;
    @FXML private Circle c9;
    @FXML private Circle c10;
    @FXML private MainController mainController;


    //Injecting MainController
    public void injectMainController(MainController mainController){this.mainController = mainController;}

    public MenuController() {
        getC1().setOnMouseDragOver(getMyHandler());

    }

    public void initialize() {

    }




    private EventHandler<MouseEvent> mouseOnContext = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Circle x  = (Circle) event.getSource();
            if (x.getId().equals(getC1().getId()))
                getC1().setFill(Color.RED);
        }
    };
    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public AnchorPane getMenu() {
        return menu;
    }

    public void setMenu(AnchorPane menu) {
        this.menu = menu;
    }

    public Circle getC1() {
        return c1;
    }

    public void setC1(Circle c1) {
        this.c1 = c1;
    }

    public Circle getC2() {
        return c2;
    }

    public void setC2(Circle c2) {
        this.c2 = c2;
    }

    public Circle getC3() {
        return c3;
    }

    public void setC3(Circle c3) {
        this.c3 = c3;
    }

    public Circle getC4() {
        return c4;
    }

    public void setC4(Circle c4) {
        this.c4 = c4;
    }

    public Circle getC5() {
        return c5;
    }

    public void setC5(Circle c5) {
        this.c5 = c5;
    }

    public Circle getC6() {
        return c6;
    }

    public void setC6(Circle c6) {
        this.c6 = c6;
    }

    public Circle getC7() {
        return c7;
    }

    public void setC7(Circle c7) {
        this.c7 = c7;
    }

    public Circle getC8() {
        return c8;
    }

    public void setC8(Circle c8) {
        this.c8 = c8;
    }

    public Circle getC9() {
        return c9;
    }

    public void setC9(Circle c9) {
        this.c9 = c9;
    }

    public Circle getC10() {
        return c10;
    }

    public void setC10(Circle c10) {
        this.c10 = c10;
    }

    public EventHandler<MouseEvent> getMyHandler() {
        return mouseOnContext;
    }

    public void setMyHandler(EventHandler<MouseEvent> myHandler) {
        this.mouseOnContext = myHandler;
    }
}
