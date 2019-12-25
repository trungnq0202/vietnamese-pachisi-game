//package controllers;
//
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.Circle;
//
//import java.util.ArrayList;
//
//public class MenuController<circle, CircleList> {
//    @FXML private AnchorPane menu;
//    @FXML private Circle c1;
//    @FXML private Circle c2;
//    @FXML private Circle c3;
//    @FXML private Circle c4;
//    @FXML private Circle c5;
//    @FXML private Circle c6;
//    @FXML private Circle c7;
//    @FXML private Circle c8;
//    @FXML private Circle c9;
//    @FXML private Circle c10;
//    private MainController mainController;
//    private int numOfPlayer;
//    private int numOfMachine;
//    private String[] color = {"31cb01", "31cb01", "31cb01", "31cb01", "31cb01",
//            "31cb01", "31cb01", "31cb01", "31cb01", "31cb01"};
//    private ArrayList<Circle> circleLists;
//
//
//    //Injecting MainController
//    public void injectMainController(MainController mainController){this.mainController = mainController;}
//
//    public MenuController() {
//
//    }
//
//    public void initialize() {
//        setCircleLists(new ArrayList<>(10));
//        addCircleToList();
//        setMouseClickedHandler();
//    }
//
//
//    private void setMouseClickedHandler() {
//        getC1().setOnMouseClicked(getMenuHandler());
//        getC2().setOnMouseClicked(getMenuHandler());
//        getC3().setOnMouseClicked(getMenuHandler());
//        getC4().setOnMouseClicked(getMenuHandler());
//        getC5().setOnMouseClicked(getMenuHandler());
//        getC6().setOnMouseClicked(getMenuHandler());
//        getC7().setOnMouseClicked(getMenuHandler());
//        getC8().setOnMouseClicked(getMenuHandler());
//        getC9().setOnMouseClicked(getMenuHandler());
//        getC10().setOnMouseClicked(getMenuHandler());
//    }
//
//    private EventHandler<MouseEvent> mouseClicked = event -> {
//        Circle x  = (Circle) event.getSource();
//        if (x.getId().equals(getC1().getId()) || x.getId().equals(getC10().getId())) {
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                if (getCircleLists().get(i).getId().equals(getC1().getId()) ||
//                        getCircleLists().get(i).getId().equals(getC10().getId())) {
//                    getColor()[i] = "d5212e"; // RED
//                }
//                if (!getCircleLists().get(i).getId().equals(getC1().getId()) &&
//                        !getCircleLists().get(i).getId().equals(getC10().getId())) {
//                    getColor()[i] = "31cb01"; // GREEN
//                }
//            }
//            updateColor();
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                System.out.println(i + "." + getColor()[i]);
//                System.out.println(getCircleLists().get(i).getId());
//            }
//            setNumOfPlayer(0);
//            setNumOfMachine(4);
//        }
//        else if (x.getId().equals(getC2().getId()) || x.getId().equals(getC9().getId())) {
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                if (getCircleLists().get(i).getId().equals(getC2().getId()) ||
//                        getCircleLists().get(i).getId().equals(getC9().getId())) {
//                    getColor()[i] = "d5212e";
//                }
//                if (!getCircleLists().get(i).getId().equals(getC2().getId()) &&
//                        !getCircleLists().get(i).getId().equals(getC9().getId())) {
//                    getColor()[i] = "31cb01";
//                }
//            }
//            updateColor();
//            setNumOfPlayer(1);
//            setNumOfMachine(3);
//        }
//        else if (x.getId().equals(getC3().getId()) || x.getId().equals(getC8().getId())) {
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                if (getCircleLists().get(i).getId().equals(getC3().getId()) ||
//                        getCircleLists().get(i).getId().equals(getC8().getId())) {
//                    getColor()[i] = "d5212e";
//                }
//                if (!getCircleLists().get(i).getId().equals(getC3().getId()) &&
//                        !getCircleLists().get(i).getId().equals(getC8().getId())) {
//                    getColor()[i] = "31cb01";
//                }
//            }
//            updateColor();
//            setNumOfPlayer(2);
//            setNumOfMachine(2);
//        }
//        else if (x.getId().equals(getC4().getId()) || x.getId().equals(getC7().getId())) {
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                if (getCircleLists().get(i).getId().equals(getC4().getId()) ||
//                        getCircleLists().get(i).getId().equals(getC7().getId())) {
//                    getColor()[i] = "d5212e";
//                }
//                if (!getCircleLists().get(i).getId().equals(getC4().getId()) &&
//                        !getCircleLists().get(i).getId().equals(getC7().getId())) {
//                    getColor()[i] = "31cb01";
//                }
//            }
//            updateColor();
//            setNumOfPlayer(3);
//            setNumOfMachine(1);
//        }
//        else if (x.getId().equals(getC5().getId()) || x.getId().equals(getC6().getId())) {
//            for (int i = 0; i < getCircleLists().size() ; i++) {
//                if (getCircleLists().get(i).getId().equals(getC5().getId()) ||
//                        getCircleLists().get(i).getId().equals(getC6().getId())) {
//                    getColor()[i] = "d5212e";
//                }
//                if (!getCircleLists().get(i).getId().equals(getC5().getId()) &&
//                        !getCircleLists().get(i).getId().equals(getC6().getId())) {
//                    getColor()[i] = "31cb01";
//                }
//            }
//            updateColor();
//            setNumOfPlayer(4);
//            setNumOfMachine(0);
//
//        }
//
//    };
//    private void addCircleToList() {
//        getCircleLists().add(getC1());
//        getCircleLists().add(getC2());
//        getCircleLists().add(getC3());
//        getCircleLists().add(getC4());
//        getCircleLists().add(getC5());
//        getCircleLists().add(getC6());
//        getCircleLists().add(getC7());
//        getCircleLists().add(getC8());
//        getCircleLists().add(getC9());
//        getCircleLists().add(getC10());
//    }
//    public void updateColor() {
//        for (int i = 0; i < getCircleLists().size() ; i++) {
//            getCircleLists().get(i).setFill(Paint.valueOf(getColor()[i]));
//        }
//    }
//
//
//    private MainController getMainController() {
//        return mainController;
//    }
//
//    private void setMainController(MainController mainController) {
//        this.mainController = mainController;
//    }
//
//    private AnchorPane getMenu() {
//        return menu;
//    }
//
//    private void setMenu(AnchorPane menu) {
//        this.menu = menu;
//    }
//
//    private Circle getC1() {
//        return c1;
//    }
//
//    private void setC1(Circle c1) {
//        this.c1 = c1;
//    }
//
//    private Circle getC2() {
//        return c2;
//    }
//
//    private void setC2(Circle c2) {
//        this.c2 = c2;
//    }
//
//    private Circle getC3() {
//        return c3;
//    }
//
//    private void setC3(Circle c3) {
//        this.c3 = c3;
//    }
//
//    private Circle getC4() {
//        return c4;
//    }
//
//    private void setC4(Circle c4) {
//        this.c4 = c4;
//    }
//
//    private Circle getC5() {
//        return c5;
//    }
//
//    private void setC5(Circle c5) {
//        this.c5 = c5;
//    }
//
//    private Circle getC6() {
//        return c6;
//    }
//
//    private void setC6(Circle c6) {
//        this.c6 = c6;
//    }
//
//    private Circle getC7() {
//        return c7;
//    }
//
//    private void setC7(Circle c7) {
//        this.c7 = c7;
//    }
//
//    private Circle getC8() {
//        return c8;
//    }
//
//    private void setC8(Circle c8) {
//        this.c8 = c8;
//    }
//
//    private Circle getC9() {
//        return c9;
//    }
//
//    private void setC9(Circle c9) {
//        this.c9 = c9;
//    }
//
//    private Circle getC10() {
//        return c10;
//    }
//
//    private void setC10(Circle c10) {
//        this.c10 = c10;
//    }
//
//    private EventHandler<MouseEvent> getMenuHandler() {
//        return mouseClicked;
//    }
//
//    private void setMenuHandler(EventHandler<MouseEvent> myHandler) {
//        this.mouseClicked = myHandler;
//    }
//
//    private int getNumOfPlayer() {
//        return numOfPlayer;
//    }
//
//    private void setNumOfPlayer(int numOfPlayer) {
//        this.numOfPlayer = numOfPlayer;
//    }
//
//    private int getNumOfMachine() {
//        return numOfMachine;
//    }
//
//    private void setNumOfMachine(int numOfMachine) {
//        this.numOfMachine = numOfMachine;
//    }
//
//    public ArrayList<Circle> getCircleLists() {
//        return circleLists;
//    }
//
//    public void setCircleLists(ArrayList<Circle> circleLists) {
//        this.circleLists = circleLists;
//    }
//
//    public String[] getColor() {
//        return color;
//    }
//
//    public void updateColor(String[] color) {
//        this.color = color;
//    }
//}

package controllers;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class MenuController{
    // Recently added
    @FXML private VBox userSetNameMenu;
    @FXML private StackPane userPane0;
    @FXML private StackPane userPane1;
    @FXML private StackPane userPane2;
    @FXML private StackPane userPane3;
    @FXML private TextField TF0;
    @FXML private TextField TF1;
    @FXML private TextField TF2;
    @FXML private TextField TF3;
    @FXML private Button backLevelBtn;
    @FXML private Button nextPlaySceneBtn;
    @FXML private Button noHumanBackBtn;
    @FXML private StackPane noHumanPane;
    @FXML private Button noHumanPlayBtn;
    @FXML private VBox noHumanPlayerVbox;
    //
    @FXML private Button exitErrorBtn;
    @FXML private VBox startMenuError;
    @FXML private Button backBtn;
    @FXML private Button nextBtn;
    @FXML private Button exitGameBtn;
    @FXML private Button newGameBtn;
    @FXML private VBox preGameMenu;
    @FXML private VBox startMenu;
    @FXML private StackPane rootMenu;
    @FXML private StackPane HP0;
    @FXML private StackPane HP1;
    @FXML private StackPane HP2;
    @FXML private StackPane HP3;
    @FXML private StackPane HP4;
    @FXML private StackPane VP0;
    @FXML private StackPane VP1;
    @FXML private StackPane VP2;
    @FXML private StackPane VP3;
    @FXML private StackPane VP4;

    private MainController mainController;  //Make connection with mainController
    private int noHumanPlayers;             //Number of human players
    private int noVirtualPlayers;            //Number of virtual players
    private static final String UNCHOSEN_COLOR = "#48da40";
    private static final String CHOSEN_COLOR = "#c93b14";
    private static final int MAX_NO_PLAYERS = 4;

    public MenuController(){
        noHumanPlayers  = 1;    //there is 1 human player by default
        noVirtualPlayers = 0;    //there is no virtual player by default
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void initialize(){
        setNoPlayersChoiceEventHandler();
        setMenuButtonsEventHandler();
    }

    //Set event handler for
    private void setNoPlayersChoiceEventHandler(){
        for (int i = 0; i <  5; i ++){
            StackPane noHumanPlayersChoice = (StackPane)rootMenu.lookup("#HP" + i);
            StackPane noVirtualPlayersChoice = (StackPane)rootMenu.lookup("#VP" + i);
            noHumanPlayersChoice.setOnMouseClicked(stkPaneOnMouseClickedEventHandler);
            noVirtualPlayersChoice.setOnMouseClicked(stkPaneOnMouseClickedEventHandler);
        }
    }

    //Set event handler for all menu buttons
    private void setMenuButtonsEventHandler(){
        setNewGameBtnEventHandler();
        setExitGameBtnEventHandler();
        setBackBtnEventHandler();
        setNextBtnEventHandler();
        setExitErrorBtnEventHandler();
        // Recently add these 3 function
//        setUserSelectionMenuTextField();
        setBackLevelBtnEventHandler();
        setNextPlaySceneBtnEventHandler();
    }

    //Event handler when clicking on the circles to choose the number of human and machine players
    private EventHandler<MouseEvent> stkPaneOnMouseClickedEventHandler = event -> {
        StackPane stackPane = (StackPane)event.getSource();  //Get the stack pane object being clicked
        //If the stack pane being clicked is a choice of a number of human players
        if (stackPane.getId().charAt(0) == 'H'){
            changeNoPlayerChoiceCirColor((StackPane)rootMenu.lookup("#HP" + noHumanPlayers), false); //paint the circle of this choice to unchosen color
            noHumanPlayers = Integer.parseInt(stackPane.getId().substring(2));  //update noHumanPlayers variable
            changeNoPlayerChoiceCirColor((StackPane)rootMenu.lookup("#HP" + noHumanPlayers), true); //paint the circle of this choice to chosen color
            if ((noHumanPlayers + noVirtualPlayers) > MAX_NO_PLAYERS) {    //If the total of players exceeded 4
                changeNoPlayerChoiceCirColor( (StackPane)rootMenu.lookup("#VP" + noVirtualPlayers) , false);   //paint the circle of this choice to unchosen color
                changeNoPlayerChoiceCirColor( (StackPane)rootMenu.lookup("#VP" + (MAX_NO_PLAYERS - noHumanPlayers)), true );  //paint the circle of this choice to chosen color
                noVirtualPlayers = MAX_NO_PLAYERS - noHumanPlayers;       //Update fixed noVirtualPlayer
            }
            //If the stack pane being clicked is a choice of a number of virtual players
        } else {
            changeNoPlayerChoiceCirColor((StackPane)rootMenu.lookup("#VP" + noVirtualPlayers), false); //paint the circle of this choice to unchosen color
            noVirtualPlayers = Integer.parseInt(stackPane.getId().substring(2));
            changeNoPlayerChoiceCirColor((StackPane)rootMenu.lookup("#VP" + noVirtualPlayers), true);   //paint the circle of this choice to chosen color
            if ((noHumanPlayers + noVirtualPlayers) > MAX_NO_PLAYERS) {    //If the total of players exceeded 4
                changeNoPlayerChoiceCirColor( (StackPane)rootMenu.lookup("#HP" + noHumanPlayers) , false);       //paint the circle of this choice to unchosen color
                changeNoPlayerChoiceCirColor( (StackPane)rootMenu.lookup("#HP" + (MAX_NO_PLAYERS - noVirtualPlayers)), true );  //paint the circle of this choice to chosen color
                noHumanPlayers = MAX_NO_PLAYERS - noVirtualPlayers;       //Update fixed noHumanPlayer
            }
        }
    };

    //Change circle color
    private void changeNoPlayerChoiceCirColor(StackPane stackPane, boolean isChosen){
        String colorCode ;  //Variable storing the code of color we will use
        if (isChosen) colorCode = CHOSEN_COLOR; //if this circle is chosen
        else colorCode = UNCHOSEN_COLOR;        //If this circle is unchosen
        Circle tempCircle = (Circle)stackPane.getChildren().get(0); //Get the circle object
        tempCircle.setStyle("-fx-fill: " + colorCode);  //Fill color in the circle
    }

    //Event handler for the newGameBtn
    private void setNewGameBtnEventHandler(){
        newGameBtn.setOnMouseClicked(event -> {
            startMenu.setVisible(false);
            preGameMenu.setVisible(true);
        });
    }

    //Event handler for the exitGameBtn
    private void setExitGameBtnEventHandler(){
        exitGameBtn.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }

    //Event handler for the exitGameBtn
    private void setBackBtnEventHandler(){
        backBtn.setOnMouseClicked(event -> {
            preGameMenu.setVisible(false);
            startMenu.setVisible(true);
        });
    }

    //Event handler for the nextBtn
    private void setNextBtnEventHandler(){
        nextBtn.setOnMouseClicked(event -> {
            if (noHumanPlayers == 0 && noVirtualPlayers == 0) {
                startMenuError.setVisible(true);
            }
            else {
                preGameMenu.setVisible(false);
                userSetNameMenu.setVisible(true);
                createSetPlayerNameMenu();
            }
        });
    }

    //Event handler for "Got it" button
    private void setExitErrorBtnEventHandler(){
        exitErrorBtn.setOnMouseClicked(event -> {
            startMenuError.setVisible(false);

        });
    }

    private void setBackLevelBtnEventHandler(){
        backLevelBtn.setOnMouseClicked(event->{
            preGameMenu.setVisible(true);
            userSetNameMenu.setVisible(false);
        });
    }

    private void setNextPlaySceneBtnEventHandler(){
        nextPlaySceneBtn.setOnMouseClicked(event->{
            userSetNameMenu.setVisible(false);
            System.exit(1);             //Jump into board game scene, delete this if necessary
        });
    }

    // Set Name Menu added
    private void createSetPlayerNameMenu() {
        for (int i = 0; i < noHumanPlayers; i++) {
            StackPane noHumanPlayerName = (StackPane)rootMenu.lookup("#userPane" + i);
            noHumanPlayerName.setVisible(true);
        }
    }


}
