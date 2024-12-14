package ru.nsu.ccfit.malinovskii.Model.Objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class WorkspaceTest {

    @Test
    void testGetSubjectByNameSuccess() {
        Workspace w = new Workspace("workspace");
        w.addSubjectByName("s1");
        w.addSubjectByName("s2");

        assertEquals("s1", w.getSubjectByName("s1").getName());
    }

    @Test
    void testGetSubjectByNameNull() {
        Workspace w = new Workspace("workspace");
        w.addSubjectByName("s1");
        w.addSubjectByName("s2");

        assertNull(w.getSubjectByName("s3"));
    }
}