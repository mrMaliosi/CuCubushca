package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.util.List;

public interface FileManager {

    void saveWorkspace(String name);

    void saveSubject(String workspaceName, String name);

    void saveTask(String workspaceName, String subjectName, String name);

    void loadWorkDir(List<Workspace> workspaces);

    void changeWorkspaceName(String oldName, String newName);

    void deleteWorkspace(String name);

    void deleteSubject(String workspaceName, String subjectName);

    void deleteSubjectTask(String workspaceName, String subjectName, String taskName);

    void changeSubjectTaskStatus(String workspaceName, String subjectName, String taskName, Status status);
}
