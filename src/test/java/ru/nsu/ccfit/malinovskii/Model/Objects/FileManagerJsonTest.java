package ru.nsu.ccfit.malinovskii.Model.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileManagerJsonTest {

    private FileManagerJson fileManager;
    private List<Workspace> workspaces;

    Path tempDir;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        this.tempDir = tempDir;
        fileManager = new FileManagerJson(tempDir.toString());
        workspaces = new ArrayList<>();

        Workspace workspace1 = new Workspace("Workspace1");
        workspace1.addSubjectByName("Subject1");
        workspace1.addTask("Subject1", "Task1");
        workspace1.addTask("Subject1", "Task2");

        Workspace workspace2 = new Workspace("Workspace2");
        workspace2.addSubjectByName("Subject2");
        workspace2.addTask("Subject2", "Task3");
        workspace2.addTask("Subject2", "Task4");

        workspaces.add(workspace1);
        workspaces.add(workspace2);
    }

    @Test
    public void testSaveAndLoad() {
        fileManager.save(workspaces);
        workspaces.clear();
        fileManager.load(workspaces);

        assertEquals(2, workspaces.size());
        System.out.println(workspaces);
        System.err.println(workspaces);

        Workspace loadedWorkspace1 = workspaces.getFirst();
        assertEquals("Workspace1", loadedWorkspace1.getName());
        assertEquals(1, loadedWorkspace1.getSubjectNames().size());
        assertEquals("Subject1", loadedWorkspace1.getSubjectNames().getFirst());

        List<Task> tasks1 = loadedWorkspace1.getSubjectTasks("Subject1");
        assertEquals(2, tasks1.size());
        assertEquals("Task1", tasks1.get(0).getName());
        assertEquals(Status.NOT_DONE, tasks1.get(0).getStatus());
        assertEquals("Task2", tasks1.get(1).getName());
        assertEquals(Status.NOT_DONE, tasks1.get(1).getStatus());

        Workspace loadedWorkspace2 = workspaces.get(1);
        assertEquals("Workspace2", loadedWorkspace2.getName());
        assertEquals(1, loadedWorkspace2.getSubjectNames().size());
        assertEquals("Subject2", loadedWorkspace2.getSubjectNames().getFirst());

        List<Task> tasks2 = loadedWorkspace2.getSubjectTasks("Subject2");
        assertEquals(2, tasks2.size());
        assertEquals("Task3", tasks2.get(0).getName());
        assertEquals(Status.NOT_DONE, tasks2.get(0).getStatus());
        assertEquals("Task4", tasks2.get(1).getName());
        assertEquals(Status.NOT_DONE, tasks2.get(1).getStatus());
    }

    @Test
    public void testLoadEmptyDirectory() {
        workspaces.clear();
        fileManager.load(workspaces);
        assertTrue(workspaces.isEmpty());
    }

    @Test
    public void testSaveAndLoadWithDifferentStatuses() {
        workspaces.getFirst().changeSubjectTaskStatus("Subject1", "Task1", Status.IN_WORK);
        workspaces.getFirst().changeSubjectTaskStatus("Subject1", "Task2", Status.PASS);

        fileManager.save(workspaces);
        workspaces.clear();
        fileManager.load(workspaces);

        assertEquals(2, workspaces.size());
        assertEquals(1, workspaces.getFirst().getSubjectNames().size());

        Workspace loadedWorkspace1 = workspaces.getFirst();
        List<Task> tasks1 = loadedWorkspace1.getSubjectTasks("Subject1");
        assertEquals(Status.IN_WORK, tasks1.get(0).getStatus());
        assertEquals(Status.PASS, tasks1.get(1).getStatus());
    }

    @Test
    public void testDeleteWorkspace() {
        fileManager.save(workspaces);
        workspaces.remove(1);
        fileManager.save(workspaces);

        workspaces.clear();
        fileManager.load(workspaces);

        assertEquals(1, workspaces.size());
    }

    @Test
    public void testDeleteSubject() {
        fileManager.save(workspaces);
        workspaces.getFirst().deleteSubject("Subject1");
        fileManager.save(workspaces);

        workspaces.clear();
        fileManager.load(workspaces);

        assertEquals(0, workspaces.getFirst().getSubjects().size());
    }

    @Test
    public void testDeleteTask() {
        assertEquals(2, workspaces.getFirst().getSubjects().getFirst().getTasks().size());

        fileManager.save(workspaces);
        workspaces.getFirst().getSubjects().getFirst().deleteTask("Task1");
        fileManager.save(workspaces);

        workspaces.clear();
        fileManager.load(workspaces);

        assertEquals(1, workspaces.getFirst().getSubjects().getFirst().getTasks().size());
    }
}