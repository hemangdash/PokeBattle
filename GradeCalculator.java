import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GradeCalculator extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    public void start(Stage mainStage) {

        mainStage.setTitle("CS 1331: Grade Calculator");

        VBox root = new VBox();
        root.setSpacing(10);

        HBox hw = new HBox();
        Label hwAverage = new Label("Homework Average: ");
        TextField hwPrompt = new TextField();
        hwPrompt.setPromptText("HW Average");
        hw.getChildren().addAll(hwAverage, hwPrompt);

        root.getChildren().addAll(hw);

        Scene scene = new Scene(root, 440, 530);
        mainStage.setScene(scene);
        mainStage.show();

    }

}