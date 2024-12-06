package ru.nsu.ccfit.malinovskii.Model.Objects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import  java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
public class Workspace {

    @Setter
    private String name;
    private List<Subject> subjects;

    public Workspace(String name) {
        this.name = name;
        subjects = new ArrayList<>();
    }

    public boolean addSubjectByName(String name) {
        for (Subject subject : subjects) {
            if (subject.getName().equals(name)) {
                return false;
            }
        }
        subjects.add(new Subject(name));
        return true;
    }

    public void addSubject(Subject subject) {
        String subjectName = subject.getName();
        for (Subject subject1 : subjects) {
            if (subject1.getName().equals(subjectName)) {
                return;
            }
        }
        subjects.add(subject);
    }

    public List<String> getSubjectNames() {
        return subjects.stream().map(Subject::getName).collect(Collectors.toList());
    }

    public boolean addTask(String subjectName, String taskName) {
        Subject targetSubject = null;
        for (Subject subject : subjects) {
            if (subject.getName().equals(subjectName)) {
                targetSubject = subject;
                break;
            }
        }

        if (targetSubject == null) {
            return false;
        }

        return targetSubject.addTaskByName(taskName);
    }

    public List<Task> getSubjectTasks(String subjectName) {
        Subject targetSubject = null;
        for (Subject subject : subjects) {
            if (subject.getName().equals(subjectName)) {
                targetSubject = subject;
                break;
            }
        }

        if (targetSubject == null) {
            return null;
        }

        return targetSubject.getTasks();
    }

    public boolean deleteSubject(String name) {
        Subject subject = null;
        for (Subject s : subjects) {
            if (s.getName().equals(name)) {
                subject = s;
                break;
            }
        }

        if (subject == null) {
            return false;
        }

        return subjects.remove(subject);
    }

    public boolean deleteSubjectTask(String subjectName, String taskName) {
        Subject subject = null;
        for (Subject s : subjects) {
            if (s.getName().equals(subjectName)) {
                subject = s;
                break;
            }
        }

        if (subject == null) {
            return false;
        }

        return subject.deleteTask(taskName);
    }

    public boolean changeSubjectTaskStatus(String subjectName, String taskName, Status status) {
        Subject subject = null;
        for (Subject s : subjects) {
            if (s.getName().equals(subjectName)) {
                subject = s;
                break;
            }
        }

        if (subject == null) {
            return false;
        }

        return subject.changeTaskStatus(taskName, status);
    }
}