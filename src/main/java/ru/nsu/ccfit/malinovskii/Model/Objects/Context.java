package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Context {
    private List<Workspace> workspaces;
    private Workspace currentWorkspace;

    public Context() {
        workspaces = new ArrayList<>();
        currentWorkspace = null;
    }

    public void loadWorkspaces(FileManager fm) {
        fm.loadWorkspaces(workspaces);
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

    public Workspace getCurrentWorkspace() {
        return currentWorkspace;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public List<String> getWorkspacesNames() {
        return workspaces.stream().map(Workspace::getName).collect(Collectors.toList());
    }

    public Workspace deleteCurrentWorkspace() {
        if (currentWorkspace == null) {
            return null;
        }
        boolean ok = workspaces.remove(currentWorkspace);
        if (!ok) {
            return null;
        }
        return currentWorkspace;
    }
}
