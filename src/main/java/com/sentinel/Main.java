package com.sentinel;

import com.sentinel.config.AppConfig;
import com.sentinel.config.WindowStateStore;
import com.sentinel.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.prefs.BackingStoreException;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final double DEFAULT_WIDTH = 1440;
    private static final double DEFAULT_HEIGHT = 900;
    private static final double MIN_WIDTH = 980;
    private static final double MIN_HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseInitializer.initialize();

        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/fxml/MainShell.fxml"))
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        for (String stylesheet : List.of(
                "/css/components.css",
                "/css/sidebar.css",
                "/css/tables.css",
                "/css/dashboard.css",
                "/css/themes.css"
        )) {
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource(stylesheet)).toExternalForm());
        }

        primaryStage.setTitle(AppConfig.APP_TITLE);
        URL iconResource = Objects.requireNonNull(getClass().getResource("/images/icons/sentinel-icon.png"));
        Image icon = new Image(iconResource.toExternalForm(), false);
        if (icon.isError()) {
            throw new IOException("Unable to load the Sentinel application icon.", icon.getException());
        }
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        WindowStateStore windowStateStore = new WindowStateStore();
        WindowStateStore.State savedState = windowStateStore.load();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(visualBounds.getWidth(), validDimension(savedState.width(), DEFAULT_WIDTH, MIN_WIDTH));
        double height = Math.min(visualBounds.getHeight(), validDimension(savedState.height(), DEFAULT_HEIGHT, MIN_HEIGHT));
        primaryStage.setMinWidth(Math.min(MIN_WIDTH, visualBounds.getWidth()));
        primaryStage.setMinHeight(Math.min(MIN_HEIGHT, visualBounds.getHeight()));

        if (hasVisibleScreenOverlap(savedState.x(), savedState.y(), width, height)) {
            primaryStage.setX(savedState.x());
            primaryStage.setY(savedState.y());
        } else {
            primaryStage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - width) / 2);
            primaryStage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - height) / 2);
        }
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        primaryStage.xProperty().addListener((observable, oldValue, newValue) ->
                saveWindowedBounds(primaryStage, windowStateStore));
        primaryStage.yProperty().addListener((observable, oldValue, newValue) ->
                saveWindowedBounds(primaryStage, windowStateStore));
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                saveWindowedBounds(primaryStage, windowStateStore));
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                saveWindowedBounds(primaryStage, windowStateStore));
        primaryStage.maximizedProperty().addListener((observable, oldValue, maximized) ->
                windowStateStore.saveMaximized(maximized));
        primaryStage.setOnCloseRequest(event -> {
            windowStateStore.saveMaximized(primaryStage.isMaximized());
            flushWindowState(windowStateStore);
        });
        primaryStage.show();
        primaryStage.setMaximized(savedState.maximized());
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static double validDimension(double value, double defaultValue, double minimum) {
        return Double.isFinite(value) && value > 0 ? Math.max(value, minimum) : defaultValue;
    }

    private static boolean hasVisibleScreenOverlap(double x, double y, double width, double height) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            return false;
        }
        Rectangle2D windowBounds = new Rectangle2D(x, y, width, height);
        return Screen.getScreens().stream().map(Screen::getVisualBounds).anyMatch(screenBounds ->
                windowBounds.intersects(screenBounds)
                        && Math.min(windowBounds.getMaxX(), screenBounds.getMaxX())
                        - Math.max(windowBounds.getMinX(), screenBounds.getMinX()) >= 200
                        && Math.min(windowBounds.getMaxY(), screenBounds.getMaxY())
                        - Math.max(windowBounds.getMinY(), screenBounds.getMinY()) >= 100
        );
    }

    private static void saveWindowedBounds(Stage stage, WindowStateStore store) {
        if (!stage.isMaximized() && stage.isShowing()) {
            store.saveWindowedBounds(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
        }
    }

    private static void flushWindowState(WindowStateStore store) {
        try {
            store.flush();
        } catch (BackingStoreException | SecurityException e) {
            logger.error("Unable to persist Sentinel window preferences.", e);
        }
    }
}
