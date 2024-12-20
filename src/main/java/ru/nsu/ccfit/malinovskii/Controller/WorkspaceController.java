package ru.nsu.ccfit.malinovskii.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nsu.ccfit.malinovskii.Model.Objects.Context;
import ru.nsu.ccfit.malinovskii.Model.Objects.Subject;
import ru.nsu.ccfit.malinovskii.Model.Objects.Workspace;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static ru.nsu.ccfit.malinovskii.Model.CuCubushca.fm;
import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class WorkspaceController {
    @FXML
    private VBox workspaceContainer;  // VBox, который будет содержать все представления
    @FXML
    private Button createSubjectButton;
    @FXML
    private ScrollPane scrollPane;

    private Workspace workspace;  // Текущая рабочая область
    Context context = getContext();

    // Метод для установки текущей рабочей области
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        initializeWorkspace();
    }

    // Метод для инициализации рабочего пространства
    public void initializeWorkspace() {
        try {
            // Очищаем контейнер перед добавлением новых предметов
            workspaceContainer.getChildren().clear();

            // Получаем список всех предметов в рабочей области
            List<Subject> subjects = workspace.getSubjects();

            for (Subject subject : subjects) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/nsu/ccfit/malinovskii/view/subject-view.fxml"));
                Parent subjectView = loader.load();

                SubjectController controller = loader.getController();
                controller.setSubject(subject);

                workspaceContainer.getChildren().add(subjectView);
            }

            scrollPane.setContent(workspaceContainer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Обработчик для создания нового предмета
    @FXML
    private void handleCreateSubject() {
        try {
            URL motivationXmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/subject-creation-view.fxml");
            FXMLLoader loader = new FXMLLoader(motivationXmlUrl);
            Parent root = loader.load();

            // Получаем контроллер из FXML
            SubjectCreationController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Создание предмета");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Модальное окно, блокирует остальные окна до закрытия
            stage.showAndWait();

            // Получаем имя рабочего пространства, которое было введено в окне
            String newName = controller.getSubjectName();
            int tasksNumber = controller.getTasksNumber();

            // Если имя было введено, добавляем рабочую область в контекст
            if (newName != null && !newName.isEmpty()) {
                Subject newSubject = new Subject(newName);
                for (int i = 1; i <= tasksNumber; ++i) {
                    newSubject.addTaskByName(String.valueOf(i));
                }
                workspace.addSubject(newSubject);
                fm.save(context.getWorkspaces());

                initializeWorkspace();
            } else {
                System.out.println("Имя рабочей области не может быть пустым!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


