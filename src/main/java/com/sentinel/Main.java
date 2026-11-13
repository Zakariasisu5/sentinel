package com.sentinel;

import com.sentinel.config.AppConfig;
import com.sentinel.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseInitializer.initialize();

        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/fxml/MainShell.fxml"))
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/css/main.css")).toExternalForm());

        primaryStage.setTitle(AppConfig.APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(720);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
