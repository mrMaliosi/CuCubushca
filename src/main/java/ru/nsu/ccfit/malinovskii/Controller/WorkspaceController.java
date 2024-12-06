package ru.nsu.ccfit.malinovskii.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import ru.nsu.ccfit.malinovskii.Model.Objects.Context;
import ru.nsu.ccfit.malinovskii.Model.Objects.Workspace;
import ru.nsu.ccfit.malinovskii.Model.Objects.Subject;
import ru.nsu.ccfit.malinovskii.Model.Objects.Task;

import java.util.List;

import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class WorkspaceController {

    @FXML
    private VBox workspaceContainer; // Контейнер для размещения всех TableView
    private Workspace workspace;  // Текущая рабочая область
    Context context = getContext();

    // Метод для создания нового TableView
    private void createTable(String subject, List<Task> tasks) {
        TableView<ObservableList<String>> tableView = new TableView<>();

        // Создание столбцов для TableView
        TableColumn<ObservableList<String>, String> subjectColumn = new TableColumn<>(subject);
        tableView.getColumns().add(subjectColumn);

        // Заполняем верхнюю строку названиями задач
        ObservableList<String> headerRow = FXCollections.observableArrayList();
        headerRow.add(subject);
        for (Task task : tasks) {
            headerRow.add(task.getName());
        }

        tableView.getItems().add(headerRow);

        // Создаем ряды с кнопками
        for (int i = 0; i < 3; i++) {
            ObservableList<String> buttonRow = FXCollections.observableArrayList();
            for (int j = 0; j <= tasks.size(); j++) {
                if (j == 0) {
                    buttonRow.add(""); // Subject в первой ячейке
                } else {
                    Button button = new Button("Button " + (i + 1) + ", " + j);
                    buttonRow.add(button.getText());
                }
            }
            tableView.getItems().add(buttonRow);
        }

        // Добавляем таблицу в контейнер
        workspaceContainer.getChildren().add(tableView);
    }

    // Метод для установки текущей рабочей области
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        initializeWorkspace();
    }

    // Метод для инициализации рабочего пространства
    public void initializeWorkspace() {
        List<String> subjectNames = workspace.getSubjectNames();

        for (String subjectName : subjectNames) {
            List<Task> tasks = workspace.getSubjectTasks(subjectName);


            // Создаем таблицу для этого предмета и задач
            createTable(subjectName, tasks);
        }
    }
}