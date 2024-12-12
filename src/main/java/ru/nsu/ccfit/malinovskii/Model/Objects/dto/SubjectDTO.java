package ru.nsu.ccfit.malinovskii.Model.Objects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private String name;
    private List<TaskDTO> tasks;
}