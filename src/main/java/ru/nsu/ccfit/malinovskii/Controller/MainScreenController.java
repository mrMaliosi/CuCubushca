package ru.nsu.ccfit.malinovskii.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.nsu.ccfit.malinovskii.Model.Objects.Workspace;

import java.io.IOException;
import java.net.URL;

import ru.nsu.ccfit.malinovskii.Model.Objects.Context;
import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;


public class MainScreenController {
    @FXML
    public Button settingsButton;
    @FXML
    public Button motivationButton;
    @FXML
    public Button addWorkspaceButton;
    @FXML
    private TableView<Workspace> workspacesTable;
    @FXML
    private TableColumn<Workspace, String> workspaces;
    @FXML
    private AnchorPane workspacePane;

    Context context = getContext();

    @FXML
    public void initialize() {
        motivationButton.setOnAction(e -> {
            try {
                URL motivationXmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/motivation-view.fxml");
                FXMLLoader loader = new FXMLLoader(motivationXmlUrl);

                // Загружаем сцену и создаем новое окно (Stage)
                Scene scene = new Scene(loader.load());

                // Получаем доступ к контроллеру мотивации
                MotivationController controller = loader.getController();

                // Создаем новый Stage для окна мотивации
                Stage stage = new Stage();
                stage.setTitle("Motivation Window"); // Заголовок окна
                stage.setScene(scene);
                stage.show(); // Показываем окно
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Логируем ошибку, если не удается загрузить FXML
            }
        });

        settingsButton.setOnAction(e -> {
            try {
                URL settingsXmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/settings-view.fxml");
                FXMLLoader loader = new FXMLLoader(settingsXmlUrl);
                Scene scene = new Scene(loader.load());
                SettingsController controller = loader.getController();

                // Создаем новый Stage для окна настроек
                Stage stage = new Stage();
                stage.setTitle("Settings Window"); // Заголовок окна
                stage.setScene(scene);
                stage.show(); // Показываем окно
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Логируем ошибку, если не удается загрузить FXML
            }
        });

        addWorkspaceButton.setOnAction(e -> {
            try {
                URL motivationXmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/workspace-creation-view.fxml");
                FXMLLoader loader = new FXMLLoader(motivationXmlUrl);
                Parent root = loader.load();

                // Получаем контроллер из FXML
                WorkspaceCreationController controller = loader.getController();

                // Создаём новое окно (Stage)
                Stage stage = new Stage();
                stage.setTitle("Создание рабочей области");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // Модальное окно, блокирует остальные окна до закрытия
                stage.showAndWait(); // Ждём закрытия окна

                // Получаем имя рабочей области, которое было введено в окне
                String newName = controller.getWorkspaceName();

                // Если имя было введено, добавляем рабочую область в контекст
                if (newName != null && !newName.isEmpty()) {
                    boolean added = context.addWorkspace(newName);
                    if (added) {
                        // Обновляем список рабочих областей в таблице
                        workspacesTable.setItems(FXCollections.observableArrayList(context.getWorkspaces()));
                    }
                } else {
                    // Можно вывести сообщение о том, что имя не было введено, или обработать ошибку
                    System.out.println("Имя рабочей области не может быть пустым!");
                }
            } catch (IOException ex) {
                // Обработка ошибок при загрузке FXML
                ex.printStackTrace();
            }
        });

        // Инициализация списка рабочих областей для таблицы
        ObservableList<Workspace> workingSpacesList = FXCollections.observableArrayList(context.getWorkspaces());

        // Устанавливаем PropertyValueFactory для привязки данных столбца к полю "name"
        workspaces.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Устанавливаем ObservableList в качестве источника данных для TableView
        workspacesTable.setItems(workingSpacesList);

        // Устанавливаем обработчик для клика по строкам таблицы
        workspacesTable.setOnMouseClicked(this::handleTableRowClick);

        //exitButton.setOnAction(e -> Platform.exit());
    }


    private void handleTableRowClick(MouseEvent event) {
        Workspace selectedWorkspace = workspacesTable.getSelectionModel().getSelectedItem();

        if (selectedWorkspace != null) {
            // Устанавливаем текущую рабочую область
            context.setCurrentWorkspace(selectedWorkspace.getName());

            // Загружаем новый FXML для рабочей области
            try {
                // Загружаем рабочую область в правую панель
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/nsu/ccfit/malinovskii/view/workspace-view.fxml"));
                Parent workspaceView = loader.load();

                // Получаем контроллер и передаем в него текущую рабочую область
                WorkspaceController controller = loader.getController();
                controller.setWorkspace(selectedWorkspace);

                // Отображаем рабочую область в правой части экрана
                workspacePane.getChildren().clear(); // Очищаем панель
                workspacePane.getChildren().add(workspaceView); // Добавляем новую рабочую область
            } catch (IOException e) {
                e.printStackTrace(); // Логируем ошибку, если не удается загрузить FXML
            }
        }
    }
}
