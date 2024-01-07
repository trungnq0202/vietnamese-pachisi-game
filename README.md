
# FinalASM_OOP



RMIT University Vietnam Course: INTE2512 Object-Oriented Programming </br>

Semester: 2019C </br>

Assessment: Final Project </br>

Team name: Team 10 </br>

Team members: Hoang Quoc Dai (3426353), Le Dinh Ngoc Quynh (3791159), Ngo Quang Trung (3742774), Bui Thanh Long (3748575), Nguyen Huynh Anh Phuong (3695662)


## Introduction

This is a Java application, “Co Ca Ngua”, which is a Vietnamese board game inspired from the Indian Parcheesi. Our game accommodates 2 - 4 players. Players can choose to play with friends or with our integrated bots. Moreover, players can also choose to play through 2 modes: online (through Internet) and offline.

  
## Features

* **Display game and its GUI components**: -   The game GUI is intuitive and easy to use. The main board closely mimics the board that you are used to playing and easy.
* **Set  up players**:  The game requires at least 2 players to play.  Before starting to play a game, players are able to set their name.
* **Roll dices**: When the players press the dices, the dices rolling animation will be played and randomized dices'value are displayed.
* **Move**: After choosing a horse and a dice value, the horse moving animation is displayed.
* **Stop**: The players are able to stop at the middle of the game. 
* **Play with bots**: Players could by machine, which is operated by a designed algorithm.
* **Score**: Each player's score is displayed in their nest and update after each of the following move: kicking other horse, going inside a home position.
* **Sound**: There is a sound button  which allows the user to enable or mute the game sound. Initially, the sound is enabled. There are 5 types of sounds in this program including horse moving sound, background music, button clicking sound, horse kicking sound, horse going outside its nest sound.
* **Play again or Quit**: After a game finishes or stopped. There are 3 buttons which are "**Play again**", "**Quit**" and   "**New Game**" being displayed. Choose "**Play again**" if you want to play another game, "**Quit**" to exit the software or "**New Game**" to setup another game from menu.
* **Game Status**: After each move, the "**Latest move description**" and 2 "**Dice values**" are updated at the top of the game board.
* **Language**: You will have the option to switch between English or Vietnamese.
*  **Networking** : Players are allowed to play together through the Internet.

## Design
The Main executable file will load the **main.fxml** file which is controlled by **MainController**. Therefore, to connect with other controllers and other **fxml** files, we need to import those **fxml** file in the **main.fxml** file and use the **initialize** function to link them together.

The starting options for the players to choose is built by scene builder in the **menu.fxml** file. In which, each pop up window will be set visible when necessary. For example, when the play offline button is clicked, the current window will be hidden and the window for choosing the amount of human vs virtual players will be visible.

By implementing this way, the amount of **fxml** file for each pop up window will be reduced.


 
## Installation

* Open the project using **Intellij IDEA 2019**.

* Setup the JDK to version 11.

### Add the JavaFX library
* From the main menu, select **```File | Project Structure```**  or   **``` Ctrl+Shift+Alt+S```**  on the toolbar.
* Specify the path to the lib folder in the JavaFX SDK package, for example: **``` C:\javafx-sdk-11.0.2\lib ```**
* In the **Choose Modules** dialog, select the **FinalASM_OOP** module  and click **OK**.

### Add VM options
* From the main menu, select **``` Run | Edit Configurations ```**.
* Select **``` Application | Main  ```** from the list on the left.
*In the **VM options** field, specify: 
`--module-path %PATH_TO_FX% --add-modules=javafx.controls,javafx.fxml,javafx.media`

Instead of `%PATH_TO_FX%`, specify the path to the JavaFX SDK lib directory, for example: 
**```C:/javafx-sdk-11.0.2/lib```**.

### Compile and Run
* Open class Main.java.

* Press **Shift + F10** or go to **Run** tools and hit **Run 'Main'**.

 * To test the **Network feature** by setting up a **localhost**, run **ServerMain** and then run at most 4 **Main** in parallel mode (can be setup in the **`Run | Edit Configurations`**)

## Known bugs

All exceptions have been handled by printing error message to the console during running the software.

## Acknowledgement

* Mr Quang's lecturer slides.

*  Package **javafx.scene.media** : [https://docs.oracle.com/javafx/2/api/javafx/scene/media/package-summary.html](https://docs.oracle.com/javafx/2/api/javafx/scene/media/package-summary.html)

