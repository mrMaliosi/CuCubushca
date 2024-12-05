package ru.nsu.ccfit.malinovskii.Model.Objects;
import lombok.Getter;

@Getter
public class Task {

    private String name;

    private Status status;

    public Task(String name) {
        this.name = name;
        status = Status.NOT_DONE;
    }

    public void setStatus(int status) {
        this.status = Status.fromValue(status);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " " + status.name();
    }
}