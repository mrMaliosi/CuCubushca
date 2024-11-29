package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.util.List;

public class ModelContext {

    private final Context ctx;

    private final FileManager fm;

    public ModelContext() {
        ctx = new Context();
        fm = new FileManager();

        ctx.loadWorkspaces(fm);
    }

    public boolean addWorkspace(String name) {
        boolean ok = ctx.addWorkspace(name);
        if (ok) {
            fm.createWorkspace(name);
        }
        return ok;
    }

    public List<String> getWorkspacesNames() {
        return ctx.getWorkspacesNames();
    }

    public boolean setCurrentWorkspace(String name) {
        return ctx.setCurrentWorkspace(name);
    }

    public boolean addSubject(String name) {
        Workspace currentWorkspace = ctx.getCurrentWorkspace();
        if (currentWorkspace == null) {
            return false;
        }

        boolean ok = currentWorkspace.addSubjectByName(name);
        if (ok) {
            fm.createSubject(currentWorkspace.getName(), name);
        }

        return ok;
    }

    public List<String> getSubjects() {
        Workspace ws = ctx.getCurrentWorkspace();
        if (ws == null) {
            return null;
        }
        return ws.getSubjectNames();
    }

    public boolean addSubjectTask(String subjectName, String taskName) {
        Workspace ws = ctx.getCurrentWorkspace();
        if (ws == null) {
            return false;
        }

        boolean ok = ws.addTask(subjectName, taskName);
        if (ok) {
            fm.createTask(ws.getName(), subjectName, taskName);
        }

        return ok;
    }

    public List<Task> getSubjectTasks(String subjectName) {
        Workspace ws = ctx.getCurrentWorkspace();
        if (ws == null) {
            return null;
        }

        return ws.getSubjectTasks(subjectName);
    }

    public boolean changeWorkspaceName(String newName) {
        Workspace ws = ctx.getCurrentWorkspace();
        if (ws == null) {
            return false;
        }

        for (String name : ctx.getWorkspacesNames()) {
            if (name.equals(newName)) {
                return false;
            }
        }

        fm.changeWorkspaceName(ws.getName(), newName);
        ws.setName(newName);

        return true;
    }

    public boolean deleteCurrentWorkspace() {
        Workspace w = ctx.deleteCurrentWorkspace();
        if (w == null) {
            return false;
        }
        fm.deleteWorkspace(w.getName());
        return true;
    }

    public boolean deleteSubject(String name) {
        Workspace w = ctx.getCurrentWorkspace();
        if (w == null) {
            return false;
        }

        boolean ok = w.deleteSubject(name);
        if (ok) {
            fm.deleteSubject(w.getName(), name);
        }
        return ok;
    }

    public boolean deleteSubjectTask(String subjectName, String taskName) {
        Workspace w = ctx.getCurrentWorkspace();
        if (w == null) {
            return false;
        }
        boolean ok = w.deleteSubjectTask(subjectName, taskName);
        if (ok) {
            fm.deleteSubjectTask(w.getName(), subjectName, taskName);
        }
        return ok;
    }
}
