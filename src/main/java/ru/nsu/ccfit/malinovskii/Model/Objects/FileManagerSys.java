package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileManagerSys implements FileManager {

    private final String workPath;

    public FileManagerSys() {
        workPath = "kukubushka";
        File newDirectory = new File(workPath);

        if (!newDirectory.exists()) {
            newDirectory.mkdir();
        }
    }

    public FileManagerSys(String workPath) {
        this.workPath = workPath;
        File newDirectory = new File(workPath);

        if (!newDirectory.exists()) {
            newDirectory.mkdir();
        }
    }

    public void saveWorkspace(String name) {
        File wsDirectory = new File(workPath, name);

        if (!wsDirectory.exists()) {
            wsDirectory.mkdir();
        }
    }

    public void saveSubject(String workspaceName, String name) {
        File wsDirectory = new File(workPath, workspaceName);
        System.out.println(wsDirectory.getPath());
        File sjDirectory = new File(wsDirectory.getPath(), name);

        if (!sjDirectory.exists()) {
            sjDirectory.mkdir();
        }
    }

    public void saveTask(String workspaceName, String subjectName, String taskName) {
        File wsDirectory = new File(workPath, workspaceName);
        File sjDirectory = new File(wsDirectory.getPath(), subjectName);
        File tsFile = new File(sjDirectory.getPath(), taskName);

        if (!tsFile.exists()) {
            try {
                tsFile.createNewFile();
                Files.writeString(tsFile.toPath(), Integer.toString(Status.NOT_DONE.getValue()));
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public void loadWorkDir(List<Workspace> workspaces) {
        Path directoryPath = Paths.get(workPath);
        try (Stream<Path> paths = Files.list(directoryPath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    Workspace workspace = new Workspace(path.getFileName().toString());
                    workspaces.add(workspace);
                    loadSubjects(workspace);
                }
            });
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void loadSubjects(Workspace workspace) {
        Path directoryPath = Paths.get(workPath + "/" + workspace.getName());
        try (Stream<Path> paths = Files.list(directoryPath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    Subject subject = new Subject(path.getFileName().toString());
                    workspace.addSubject(subject);
                    loadTasks(subject, workspace.getName());
                }
            });
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void loadTasks(Subject subject, String workspaceName) {
        Path directoryPath = Paths.get(workPath + "/" + workspaceName + "/" + subject.getName());
        try (Stream<Path> paths = Files.list(directoryPath)) {
            paths.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    Task task = new Task(path.getFileName().toString());
                    try {
                        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        if (!lines.isEmpty()) {
                            String firstLine = lines.get(0);
                            if (!firstLine.isEmpty()) {
                                char firstCharacter = firstLine.charAt(0);
                                Status status = Status.fromValue(Character.getNumericValue(firstCharacter));
                                task.setStatus(status);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }

                    subject.addTask(task);
                }
            });
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void changeWorkspaceName(String oldName, String newName) {
        Path oldPath = Paths.get(workPath + "/" + oldName);
        Path newPath = Paths.get(workPath + "/" + newName);

        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deleteWorkspace(String name) {
        Path directoryPath = Paths.get(workPath + "/" + name);
        try {
            deleteDirectory(directoryPath);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deleteSubject(String workspaceName, String subjectName) {
        Path directoryPath = Paths.get(workPath + "/" + workspaceName + "/" + subjectName);
        try {
            deleteDirectory(directoryPath);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deleteSubjectTask(String workspaceName, String subjectName, String taskName) {
        Path path = Paths.get(workPath + "/" + workspaceName + "/" + subjectName + "/" + taskName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void deleteDirectory(Path directoryPath) throws IOException {
        if (Files.exists(directoryPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        deleteDirectory(path);
                    } else {
                        Files.delete(path);
                    }
                }
            }
            Files.delete(directoryPath);
        }
    }

    public void changeSubjectTaskStatus(String workspaceName, String subjectName, String taskName, Status status) {
        Path taskPath = Paths.get(workPath + "/" + workspaceName + "/" + subjectName);
        File tsFile = new File(taskPath.toString(), taskName);

        if (tsFile.exists()) {
            try {
                Files.writeString(tsFile.toPath(), Integer.toString(status.getValue()));
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}