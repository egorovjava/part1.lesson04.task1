package com.gmail.egorovsonalexey;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.*;

public class ObjectTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ArrayList<String> objectFields;

    public ObjectTest() {
        String[] fieldsArray = {"100", "true", "x", "Test string"};
        objectFields = new ArrayList<>();
        Collections.addAll(objectFields, fieldsArray);
    }

    @Test
    public void outputTestAllFields() {

        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("intField");
        fieldsToOutput.add("boolField");
        fieldsToOutput.add("charField");
        fieldsToOutput.add("strField");
        TestClass testClass = new TestClass();

        try {
            System.setOut(new PrintStream(outContent));
            App.cleanup(testClass, fieldsToCleanup, fieldsToOutput);
        } finally {
            System.setOut(new PrintStream(originalOut));
        }

        String result = outContent.toString();
        String[] lines = result.split("\r\n");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, lines);
        assertTrue(list.containsAll(objectFields));
        assertTrue(objectFields.containsAll(list));
    }

    @Test
    public void outputTestOneField() {

        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("intField");
        TestClass testClass = new TestClass();

        try {
            System.setOut(new PrintStream(outContent));
            App.cleanup(testClass, fieldsToCleanup, fieldsToOutput);
        } finally {
            System.setOut(new PrintStream(originalOut));
        }

        String result = outContent.toString();
        String[] lines = result.split("\r\n");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, lines);
        assertTrue(list.contains("100"));
        assertEquals(list.size(), 1);
    }


    @Test
    public void cleanUpTestAllFields() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToCleanup.add("intField");
        fieldsToCleanup.add("boolField");
        fieldsToCleanup.add("charField");
        fieldsToCleanup.add("strField");
        TestClass ob = new TestClass();
        App.cleanup(ob, fieldsToCleanup, fieldsToOutput);

        assertEquals(ob.charField, (char) 0);
        assertFalse(ob.boolField);
        assertEquals(ob.intField, 0);
        assertNull(ob.strField);
    }

    @Test
    public void cleanUpTestOneField() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToCleanup.add("intField");
        TestClass ob = new TestClass();
        App.cleanup(ob, fieldsToCleanup, fieldsToOutput);

        assertEquals(ob.charField, 'x');
        assertTrue(ob.boolField);
        assertEquals(ob.intField, 0);
        assertEquals(ob.strField, objectFields.get(3));
    }

    @Test
    public void emptyTest() {

        TestClass ob = new TestClass();
        App.cleanup(ob, null, null);

        assertEquals(ob.charField, 'x');
        assertTrue(ob.boolField);
        assertEquals(ob.intField, 100);
        assertEquals(ob.strField, objectFields.get(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fieldMissingTest1() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("wrongFieldName");
        TestClass ob = new TestClass();
        App.cleanup(ob, fieldsToCleanup, fieldsToOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fieldMissingTest2() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToCleanup.add("wrongFieldName");
        TestClass ob = new TestClass();
        App.cleanup(ob, fieldsToCleanup, fieldsToOutput);
    }
}
