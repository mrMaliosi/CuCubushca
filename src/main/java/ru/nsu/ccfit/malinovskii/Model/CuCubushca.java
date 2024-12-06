package ru.nsu.ccfit.malinovskii.Model;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.ccfit.malinovskii.Model.Objects.Context;
import ru.nsu.ccfit.malinovskii.Model.Objects.FileManager;
import ru.nsu.ccfit.malinovskii.Model.Objects.FileManagerSys;

import java.io.*;
import java.net.URL;

import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class CuCubushca extends Application {
    public static Stage stage;
    public static FXMLLoader loader = new FXMLLoader();
    public static FileManager fm = new FileManagerSys();
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        URL xmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/main-view.fxml");
        loader.setLocation(xmlUrl);
        Context context = getContext();
        context.initializeWorkspaces(fm);
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("CuCubushca");
            stage.show();
        } catch (IOException e) {
            // Обработка ошибок, связанных с загрузкой FXML файла
            System.err.println("Ошибка при загрузке FXML файла: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Обработка других неожиданных ошибок
            System.err.println("Неизвестная ошибка: " + e.getMessage());
            e.printStackTrace();
        }

    }


    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
