package helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class SceneTransfer {
     protected Stage stage;
    protected Parent root;
    protected Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    protected void transferScene(ActionEvent event, String sceneName) throws IOException {
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(sceneName));
        stage.setScene(new Scene(root));
        stage.show();

    }


//    protected void transferScene(String sceneName) throws IOException {
//
////        stage = (Stage) node.getScene().getWindow();
//        scene = FXMLLoader.load(getClass().getResource(sceneName));
//        stage.setScene(new Scene(root));
//        stage.show();
//
//    }
@FXML
    protected void transferScene2(ActionEvent event, String sceneName2) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(sceneName2));
        stage.setScene(new Scene(root));
        stage.show();
    }



}
