package ru.nsu.ccfit.malinovskii.Model.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.ccfit.malinovskii.Model.Objects.dto.SubjectDTO;
import ru.nsu.ccfit.malinovskii.Model.Objects.dto.TaskDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileManagerJson implements FileManager {

    private final ObjectMapper objectMapper;
    private final String baseDirectory;

    public FileManagerJson() {
        this.baseDirectory = "kukubushka";
        this.objectMapper = new ObjectMapper();

        File newDirectory = new File(baseDirectory);

        if (!newDirectory.exists()) {
            newDirectory.mkdirs();
        }
    }

    public FileManagerJson(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.objectMapper = new ObjectMapper();

        File newDirectory = new File(baseDirectory);

        if (!newDirectory.exists()) {
            newDirectory.mkdirs();
        }
    }

    @Override
    public void save(List<Workspace> workspaces) {
        Set<String> currentWorkspaceNames = workspaces.stream()
                .map(Workspace::getName)
                .collect(Collectors.toSet());

        File baseDir = new File(baseDirectory);
        if (baseDir.exists() && baseDir.isDirectory()) {
            for (File workspaceDir : baseDir.listFiles()) {
                if (workspaceDir.isDirectory() && !currentWorkspaceNames.contains(workspaceDir.getName())) {
                    deleteDirectory(workspaceDir);
                }
            }
        }

        for (Workspace workspace : workspaces) {
            String workspaceDir = baseDirectory + File.separator + workspace.getName();
            File dir = new File(workspaceDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Set<String> currentSubjectNames = new HashSet<>(workspace.getSubjectNames());

            if (dir.exists() && dir.isDirectory()) {
                for (File subjectFile : dir.listFiles()) {
                    if (subjectFile.isFile() && subjectFile.getName().endsWith(".json") &&
                            !currentSubjectNames.contains(subjectFile.getName().replace(".json", ""))) {
                        subjectFile.delete();
                    }
                }
            }

            for (Subject subject : workspace.getSubjects()) {
                String subjectFileName = workspaceDir + File.separator + subject.getName() + ".json";
                try {
                    List<TaskDTO> taskDTOs = new ArrayList<>();
                    for (Task task : subject.getTasks()) {
                        taskDTOs.add(new TaskDTO(task.getName(), task.getStatus().getValue()));
                    }
                    SubjectDTO subjectDTO = new SubjectDTO(subject.getName(), taskDTOs);
                    objectMapper.writeValue(new File(subjectFileName), subjectDTO);
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    @Override
    public void load(List<Workspace> workspaces) {
        File baseDir = new File(baseDirectory);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return;
        }

        for (File workspaceDir : baseDir.listFiles()) {
            if (workspaceDir.isDirectory()) {
                Workspace workspace = new Workspace(workspaceDir.getName());
                workspaces.add(workspace);

                for (File subjectFile : workspaceDir.listFiles()) {
                    if (subjectFile.getName().endsWith(".json")) {
                        try {
                            SubjectDTO subjectDTO = objectMapper.readValue(subjectFile, SubjectDTO.class);
                            Subject subject = new Subject(subjectDTO.getName());
                            workspace.addSubject(subject);

                            for (TaskDTO taskDTO : subjectDTO.getTasks()) {
                                Task task = new Task(taskDTO.getName());
                                task.setStatus(taskDTO.getStatus());
                                subject.addTask(task);
                            }
                        } catch (IOException e) {
                            e.printStackTrace(System.err);
                        }
                    }
                }
            }
        }
    }
}