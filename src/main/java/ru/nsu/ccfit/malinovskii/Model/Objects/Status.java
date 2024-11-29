package ru.nsu.ccfit.malinovskii.Model.Objects;

public enum Status {
    NOT_DONE(0),
    IN_WORK(1),
    PASS(2);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Status fromValue(int value) {
        for (Status status : Status.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }
}