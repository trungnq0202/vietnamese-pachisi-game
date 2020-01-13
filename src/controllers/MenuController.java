package controllers;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import models.MatchInformation;
import models.Player;
import models.Sound;


import java.io.IOException;
import java.util.ArrayList;

public class MenuController{
    // Recently added
    public Button onlineGameBtn;
    public Button offlineGameBtn;
    public VBox onlinePlayMenu;
    public TextField onlinePlayerTextField;
    public Button backToMainMenuBtn;
    public Button onlinePlayBtn;

    private static MenuController menuController;
    private ClientController clientController;

    //Old ones
    @FXML private VBox userSetNameMenu;
    @FXML private Button backLevelBtn;
    @FXML private Button nextPlaySceneBtn;
    @FXML private Button exitErrorBtn1;
    @FXML private VBox emptyPlayerNameError;
    @FXML private Button exitErrorBtn;
    @FXML private VBox cantConnectToServerError;
    @FXML private Button cantConnectToServerBtn;
    @FXML private VBox startMenuError;
    @FXML private Button backBtn;
    @FXML private Button nextBtn;
    @FXML private Button exitGameBtn;

    //    @FXML private Button newGameBtn;
    @FXML private VBox preGameMenu;
    @FXML private VBox startMenu;
    @FXML private StackPane rootMenu;
    private MainController mainController;  //Make connection with mainController
    private int noHumanPlayers;             //Number of human players
    private int noVirtualPlayers;            //Number of virtual players
    private Sound btnClickSound;

    private ArrayList<String> playersNameList;
    private static final String UNCHOSEN_COLOR = "#48da40";
    private static final String CHOSEN_COLOR = "#c93b14";
    private static final int MAX_NO_PLAYERS = 4;

    public MenuController(){
        System.out.println("menucontroller construct");
        playersNameList = new ArrayList<>() {
            {
                for (int i = 0; i < 4; i++) add(" ");
            }
        };
        noHumanPlayers  = 2;    //there is 1 human player by default
        noVirtualPlayers = 0;    //there is no virtual player by default
        btnClickSound = new Sound(Sound.SoundType.BUTTON_CLICK_SFX);
    }

    void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML public void initialize(){
        System.out.println("menucontroller init");
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
        setOfflineGameBtnEventHandler();
        setExitGameBtnEventHandler();
        setBackBtnEventHandler();
        setNextBtnEventHandler();
        setExitErrorBtnEventHandler();

        //recently added for online players
        setOnlineGameBtnEventHandler();
        setOnlinePlayBtnEventHandler();
        setBackToMainMenuBtnEventHandler();

        // Recently add these 3 function
//        setUserSelectionMenuTextField();
        setBackLevelBtnEventHandler();
        setNextPlaySceneBtnEventHandler();
    }

    //Event handler when clicking on the circles to choose the number of human and machine players
    private EventHandler<MouseEvent> stkPaneOnMouseClickedEventHandler = event -> {
        btnClickSound.play();
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

    private void resetPlayersNameList() {
        playersNameList.set(0," ");
        playersNameList.set(1," ");
        playersNameList.set(2," ");
        playersNameList.set(3," ");
    }

    //Set value for each object in playersNameList from textfield
    private void setPlayersNameList(){
        //Name of the human players
        for (int i = 0;  i < noHumanPlayers; i++){
            TextField tempPlayerNameTF =  (TextField)rootMenu.lookup("#TF" + i);
            playersNameList.set(i, tempPlayerNameTF.getText());
        }
        //Name of the virtual players
        for (int i = noHumanPlayers; i < noHumanPlayers + noVirtualPlayers ; i++){
            playersNameList.set(i, "Virtual Player " + (i - noHumanPlayers + 1) );
        }
    }

    private void setOnlinePlayersNameList(ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            this.playersNameList.set(i, players.get(i).getName());
        }
    }

    ArrayList<String> getPlayersNameList() {
        return playersNameList;
    }

    //Event handler for the newGameBtn
    private void setOfflineGameBtnEventHandler(){
        offlineGameBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            startMenu.setVisible(false);     //hide start menu
            preGameMenu.setVisible(true);    //show pregame menu
        });
    }

    //Event handler for the exitGameBtn
    private void setExitGameBtnEventHandler(){
        exitGameBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            System.exit(0);         //Exit the program
        });
    }

    //Event handler for the get back to start menu GameBtn
    private void setBackBtnEventHandler(){
        backBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            preGameMenu.setVisible(false);  //hide pregame menu
            startMenu.setVisible(true);     //show start menu
        });
    }

    //Event handler for the nextBtn
    private void setNextBtnEventHandler(){
        nextBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            if (noHumanPlayers + noVirtualPlayers < 2) {
                startMenuError.setVisible(true);
            }
            else {
                resetPlayersNameList();
                preGameMenu.setVisible(false);
                userSetNameMenu.setVisible(true);
                createSetPlayerNameMenu();
            }
        });
    }

    //Event handler for "Got it" button
    private void setExitErrorBtnEventHandler(){
        exitErrorBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            startMenuError.setVisible(false);

        });

        exitErrorBtn1.setOnMouseClicked(event -> {
            btnClickSound.play();
            emptyPlayerNameError.setVisible(false);
        });

        cantConnectToServerBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            cantConnectToServerError.setVisible(false);
        });
    }

    private void setBackLevelBtnEventHandler(){
        backLevelBtn.setOnMouseClicked(event->{
            btnClickSound.play();
            preGameMenu.setVisible(true);
            userSetNameMenu.setVisible(false);
        });
    }

    //Check if the user input nothing for the players'name
    private boolean validatePlayersNameInput(){
        for (int i = 0; i < noHumanPlayers; i++ ){
            TextField tmpPlayerNameTF = (TextField)rootMenu.lookup("#TF" + i);
            if (tmpPlayerNameTF.getText().equals("")) return false;
        }
        return true;
    }

    private void setNextPlaySceneBtnEventHandler(){
        nextPlaySceneBtn.setOnMouseClicked(event->{
            btnClickSound.play();
            if (!validatePlayersNameInput()){
                emptyPlayerNameError.setVisible(true);
            }else {
                setPlayersNameList();
                userSetNameMenu.setVisible(false);
                mainController.displayGameBoard(true);
            }
        });
    }

    public void startOnlineGame(MatchInformation matchInformation) {
        setOnlinePlayersNameList(matchInformation.getPlayers());
        System.out.println(mainController);
        this.mainController.displayGameBoard(true);
    }

    // Set Name Menu added
    private void createSetPlayerNameMenu() {
        //Show text field for inputting human players' name
        for (int i = 0; i < noHumanPlayers; i++) {
            StackPane humanPlayerNamePane = (StackPane)rootMenu.lookup("#userPane" + i);
            humanPlayerNamePane.setVisible(true);
        }

        //Show disabled text field with virtual players'name default as "Virtual Player + (i - noHumanPlayers + 1)"
        for (int i = noHumanPlayers; i < noHumanPlayers + noVirtualPlayers; i++ ){
            StackPane virtualPlayerNamePane = (StackPane)rootMenu.lookup("#userPane" + i);
            virtualPlayerNamePane.setVisible(true);
            TextField virtualPlayerNameTF = (TextField)virtualPlayerNamePane.getChildren().get(1);
            virtualPlayerNameTF.setText("Virtual Player " + (i - noHumanPlayers + 1));
            virtualPlayerNameTF.setDisable(true);
        }

        //Hide those text field if noHumanPlayers + noVirtualPlayers < 4
        for (int i = noHumanPlayers + noVirtualPlayers; i < 4; i++){
            StackPane playerNamePane = (StackPane)rootMenu.lookup("#userPane" + i);
            playerNamePane.setVisible(false);
        }
    }

    int getNoHumanPlayers() {
        return noHumanPlayers;
    }

    int getNoVirtualPlayers() {
        return noVirtualPlayers;
    }


    //recently added
    private void setOnlineGameBtnEventHandler(){
        onlineGameBtn.setOnMouseClicked(event -> {
            btnClickSound.play();
            try {
                this.clientController = new ClientController();
                clientController.start();
            } catch (IOException e) {
                cantConnectToServerError.setVisible(true);
                return;
            }
            startMenu.setVisible(false);           //hide start menu
            onlinePlayMenu.setVisible(true);       //online play menu
        });
    }

    //create event handler for online play button
    private void setOnlinePlayBtnEventHandler(){
        onlinePlayBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            //check if text field is empty -> show error
            if(onlinePlayerTextField.getText().equals("")){
                emptyPlayerNameError.setVisible(true);
            }
            else {
                if (onlinePlayerTextField.getText() != null) {

                    //create new player with given name
                    //update game connection field
                    Platform.runLater(() -> {
                        this.clientController.ready(onlinePlayerTextField.getText());
                        onlinePlayBtn.setMouseTransparent(true);
                        onlinePlayBtn.setText("Waiting...");
                        onlinePlayerTextField.setDisable(true);
                    });
                }
            }
        });
    }

    private void setBackToMainMenuBtnEventHandler() {
        backToMainMenuBtn.setOnMouseClicked(mouseEvent -> {
            btnClickSound.play();
            this.clientController.disconnect();
            this.clientController = null;
            this.onlinePlayMenu.setVisible(false);
            this.startMenu.setVisible(true);
            onlinePlayBtn.setMouseTransparent(false);
            onlinePlayBtn.setText("Ready");
            onlinePlayerTextField.setDisable(false);
        });
    }

    public static MenuController getMenuController(){
        if (menuController == null){
            menuController = new MenuController();
        } return menuController;
    }
}