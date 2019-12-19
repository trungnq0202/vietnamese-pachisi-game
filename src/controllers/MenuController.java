package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    private MainController mainController;
    private int numOfPlayer;
    private int numOfMachine;


    //Injecting MainController
    public void injectMainController(MainController mainController){this.mainController = mainController;}

    public MenuController() {
        getC1().setOnMouseClicked(getMenuHandler());
        getC2().setOnMouseClicked(getMenuHandler());
        getC3().setOnMouseClicked(getMenuHandler());
        getC4().setOnMouseClicked(getMenuHandler());
        getC5().setOnMouseClicked(getMenuHandler());
        getC6().setOnMouseClicked(getMenuHandler());
        getC7().setOnMouseClicked(getMenuHandler());
        getC8().setOnMouseClicked(getMenuHandler());
        getC9().setOnMouseClicked(getMenuHandler());
        getC10().setOnMouseClicked(getMenuHandler());


    }

    public void initialize() {

    }




    private EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Circle x  = (Circle) event.getSource();
            if (x.getId().equals(getC1().getId()) || x.getId().equals(getC10().getId())) {
                getC1().setFill(Color.RED);
                getC10().setFill(Color.RED);
                setNumOfPlayer(0);
                setNumOfMachine(4);
            }
            else if (x.getId().equals(getC2().getId()) || x.getId().equals(getC9().getId())) {
                getC2().setFill(Color.RED);
                getC9().setFill(Color.RED);
                setNumOfPlayer(1);
                setNumOfMachine(3);
            }
            else if (x.getId().equals(getC3().getId()) || x.getId().equals(getC8().getId())) {
                getC3().setFill(Color.RED);
                getC8().setFill(Color.RED);
                setNumOfPlayer(2);
                setNumOfMachine(2);
            }
            else if (x.getId().equals(getC4().getId()) || x.getId().equals(getC7().getId())) {
                getC4().setFill(Color.RED);
                getC7().setFill(Color.RED);
                setNumOfPlayer(3);
                setNumOfMachine(1);
            }
            else if (x.getId().equals(getC5().getId()) || x.getId().equals(getC6().getId())) {
                getC5().setFill(Color.RED);
                getC6().setFill(Color.RED);
                setNumOfPlayer(4);
                setNumOfMachine(0);
            }
        }
    };
    private MainController getMainController() {
        return mainController;
    }

    private void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private AnchorPane getMenu() {
        return menu;
    }

    private void setMenu(AnchorPane menu) {
        this.menu = menu;
    }

    private Circle getC1() {
        return c1;
    }

    private void setC1(Circle c1) {
        this.c1 = c1;
    }

    private Circle getC2() {
        return c2;
    }

    private void setC2(Circle c2) {
        this.c2 = c2;
    }

    private Circle getC3() {
        return c3;
    }

    private void setC3(Circle c3) {
        this.c3 = c3;
    }

    private Circle getC4() {
        return c4;
    }

    private void setC4(Circle c4) {
        this.c4 = c4;
    }

    private Circle getC5() {
        return c5;
    }

    private void setC5(Circle c5) {
        this.c5 = c5;
    }

    private Circle getC6() {
        return c6;
    }

    private void setC6(Circle c6) {
        this.c6 = c6;
    }

    private Circle getC7() {
        return c7;
    }

    private void setC7(Circle c7) {
        this.c7 = c7;
    }

    private Circle getC8() {
        return c8;
    }

    private void setC8(Circle c8) {
        this.c8 = c8;
    }

    private Circle getC9() {
        return c9;
    }

    private void setC9(Circle c9) {
        this.c9 = c9;
    }

    private Circle getC10() {
        return c10;
    }

    private void setC10(Circle c10) {
        this.c10 = c10;
    }

    private EventHandler<MouseEvent> getMenuHandler() {
        return mouseClicked;
    }

    private void setMenuHandler(EventHandler<MouseEvent> myHandler) {
        this.mouseClicked = myHandler;
    }

    private int getNumOfPlayer() {
        return numOfPlayer;
    }

    private void setNumOfPlayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }

    private int getNumOfMachine() {
        return numOfMachine;
    }

    private void setNumOfMachine(int numOfMachine) {
        this.numOfMachine = numOfMachine;
    }


}
