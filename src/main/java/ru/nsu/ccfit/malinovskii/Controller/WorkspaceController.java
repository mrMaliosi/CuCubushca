package ru.nsu.ccfit.malinovskii.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import ru.nsu.ccfit.malinovskii.Model.Objects.Status;
import ru.nsu.ccfit.malinovskii.Model.Objects.Subject;
import ru.nsu.ccfit.malinovskii.Model.Objects.Task;
import ru.nsu.ccfit.malinovskii.Model.Objects.Workspace;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceController {
    @FXML
    private TableView<Subject> subjectsTable;
    @FXML
    private TableColumn<Subject, String> subjectNameColumn;
    @FXML
    private TableColumn<Subject, Task> task1Column;
    @FXML
    private TableColumn<Subject, Task> task2Column;
    @FXML
    private TableColumn<Subject, Task> task3Column;

    private List<Subject> subjects = new ArrayList<>();

    Workspace currentWorkspace;

    // Инициализация
    @FXML
    public void initialize() {

    }

    public void setWorkspace(Workspace workspace){
        this.currentWorkspace = workspace;
        subjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Устанавливаем обработчик для первого задания
        task1Column.setCellFactory(column -> new TableCell<Subject, Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Button taskButton = createTaskButton(task, 1);
                    setGraphic(taskButton);
                }
            }
        });

        // Устанавливаем обработчик для второго задания
        task2Column.setCellFactory(column -> new TableCell<Subject, Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Button taskButton = createTaskButton(task, 2);
                    setGraphic(taskButton);
                }
            }
        });

        // Устанавливаем обработчик для третьего задания
        task3Column.setCellFactory(column -> new TableCell<Subject, Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Button taskButton = createTaskButton(task, 3);
                    setGraphic(taskButton);
                }
            }
        });

        // Устанавливаем список предметов в таблицу
        subjectsTable.setItems(FXCollections.observableArrayList(subjects));
    }

    // Создание кнопки для задания, которая будет менять цвет и статус
    private Button createTaskButton(Task task, int taskNumber) {
        Button button = new Button();
        button.setMinWidth(60);
        button.setText("Статус " + taskNumber);

        // Устанавливаем цвет в зависимости от статуса задания
        updateButtonColor(button, task);

        button.setOnAction(e -> {
            updateTaskStatus(task, taskNumber);
            updateButtonColor(button, task);
        });

        return button;
    }

    // Обновление цвета кнопки в зависимости от статуса
    private void updateButtonColor(Button button, Task task) {
        switch (task.getStatus()) {
            case NOT_DONE -> button.setStyle("-fx-base: white;");
            case IN_WORK -> button.setStyle("-fx-base: yellow;");
            case PASS -> button.setStyle("-fx-base: green;");
        }
    }

    // Обновление статуса задания
    private void updateTaskStatus(Task task, int taskNumber) {
        int currentStatus = task.getStatus().getValue();
        if (currentStatus < taskNumber) {
            task.setStatus(taskNumber);  // Меняем статус на 1, 2 или 3 в зависимости от клика
        } else if (currentStatus == taskNumber) {
            task.setStatus(0);  // Если нажать на ту же кнопку, сбрасываем статус
        }
    }

    public Workspace getWorkspace () {
        return currentWorkspace;
    }
}


/*
    // Пример данных для предметов и заданий
    private void initializeExampleSubjects() {
        List<Task> tasks1 = new ArrayList<>();
        tasks1.add(new Task("Задание 1"));
        tasks1.add(new Task("Задание 2"));
        tasks1.add(new Task("Задание 3"));

        List<Task> tasks2 = new ArrayList<>();
        tasks2.add(new Task("Задание 1"));
        tasks2.add(new Task("Задание 2"));
        tasks2.add(new Task("Задание 3"));

        subjects.add(new Subject("Математика", tasks1));
        subjects.add(new Subject("Физика", tasks2));
    }
 */

