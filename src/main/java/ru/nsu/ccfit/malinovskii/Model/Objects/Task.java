package ru.nsu.ccfit.malinovskii.Model.Objects;

public class Task {
    private String name;
    private int status;

    public Task(String name) {
        this.name = name;
        status = 0;
    }

    public void changeStatus(int newStatus) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //@Override
    //public String toString() {
    //    return name + " " + status.name();
    //}
}