package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String name;
    private List<Task> tasks;

    public Subject(String name) {
        this.name = name;
        tasks = new ArrayList<>();
    }

    public Subject(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public boolean addTaskByName(String name) {
        for (Task t : tasks) {
            if (t.getName().equals(name)) {
                return false;
            }
        }
        tasks.add(new Task(name));
        return true;
    }

    public boolean addTask(Task task) {
        String taskName = task.getName();
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                return false;
            }
        }
        tasks.add(task);
        return true;
    }

    public boolean deleteTask(String name) {
        for (Task task : tasks) {
            if (task.getName().equals(name)) {
                tasks.remove(task);
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}