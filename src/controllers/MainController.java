package controllers;

public class MainController {
    private StartSceneController startSceneController;

//    private void initialize(){
//        injectMainControllerInNestedControllers();
//    }

//    private void injectMainControllerInNestedControllers(){
//        getStart().injectMainController(this);
//    }

    public StartSceneController getStart() {
        return startSceneController;
    }

    public void setStartSceneController(StartSceneController startSceneController) {
        this.startSceneController = startSceneController;
    }
}
