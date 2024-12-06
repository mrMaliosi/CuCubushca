package ru.nsu.ccfit.malinovskii.Model.Objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Глобальный синглтон-класс
@Getter
public class Context {
    private static Context context;  // Приватная статическая переменная для хранения единственного экземпляра
    private final List<Workspace> workspaces;
    private Workspace currentWorkspace;

    private Context() {
        workspaces = new ArrayList<>();
        currentWorkspace = null;
    }

    // Метод для получения единственного экземпляра
    public static Context getContext() {
        if (context == null) {
            synchronized (Context.class) {          // Синхронизация для обеспечения потокобезопасности (задел на будущее)
                if (context == null) {
                    context = new Context();     // Создание экземпляра, если его нет
                }
            }
        }
        return context;
    }

    public void initializeWorkspaces(FileManager fm){
        fm.loadWorkDir(workspaces);
    }

    public boolean addWorkspace(String name) {
        for (Workspace workspace : workspaces) {
            if (workspace.getName().equals(name)) {
                return false;
            }
        }
        workspaces.add(new Workspace(name));
        return true;
    }

    public boolean setCurrentWorkspace(String name) {
        for (Workspace workspace : workspaces) {
            if (workspace.getName().equals(name)) {
                currentWorkspace = workspace;
                return true;
            }
        }

        return false;
    }

    public List<String> getWorkspacesNames() {
        return workspaces.stream().map(Workspace::getName).collect(Collectors.toList());
    }

    public boolean deleteCurrentWorkspace() {
        if (currentWorkspace == null) {
            return false;
        }
        return workspaces.remove(currentWorkspace);
    }
}
