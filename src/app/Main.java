package app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("windows/singleSimulation.fxml"));
        Parent root = loader.load();
        //Controller controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin Simulator");
        primaryStage.getIcons().add(new Image("file:resources/icon.png"));
        primaryStage.show();
        */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("windows/paramSelection.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Darwin Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
