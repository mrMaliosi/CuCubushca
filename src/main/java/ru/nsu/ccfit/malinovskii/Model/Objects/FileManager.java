package ru.nsu.ccfit.malinovskii.Model.Objects;

import java.util.List;

public interface FileManager {

    void save(List<Workspace> workspaces);

    void load(List<Workspace> workspaces);
}
