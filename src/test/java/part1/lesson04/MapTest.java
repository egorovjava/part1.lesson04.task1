package part1.lesson04;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MapTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public MapTest() {

    }

    private HashMap<String, Object> getTestMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("intKey", 100);
        map.put("boolKey", true);
        map.put("charKey", 'x');
        map.put("strKey", "Test string");
        return map;
    }

    @Test
    public void outputTest() {
        HashMap<String, Object> map = getTestMap();
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>(map.keySet());

        try {
            System.setOut(new PrintStream(outContent));
            App.cleanup(map, fieldsToCleanup, fieldsToOutput);
        } finally {
            System.setOut(new PrintStream(originalOut));
        }
        Collection<String> values =
                map.values().stream().map(x -> x == null ? "null" : x.toString()).collect(Collectors.toList());
        String result = outContent.toString();
        String[] lines = result.split("\r\n");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, lines);
        assertTrue(values.containsAll(list));
        assertTrue(list.containsAll(values));
    }

    @Test
    public void cleanUpTest() {
        HashMap<String, Object> map = new HashMap<>();
        HashSet<String> fieldsToCleanup = new HashSet<>(map.keySet());
        HashSet<String> fieldsToOutput = new HashSet<>();

        App.cleanup(map, fieldsToCleanup, fieldsToOutput);

        assertEquals(map.size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fieldMissingTest1() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("wrongKey");
        HashMap<String, Object> map = getTestMap();
        App.cleanup(map, fieldsToCleanup, fieldsToOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fieldMissingTest2() {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToCleanup.add("wrongKey");
        HashMap<String, Object> map = getTestMap();
        App.cleanup(map, fieldsToCleanup, fieldsToOutput);
    }

    @Test
    public void newMapTest() {
        NewMap newMap = new NewMap();
        NewMap map = new NewMap();
        map.put("intKey", 100);
        map.put("boolKey", true);
        map.put("charKey", 'x');
        map.put("strKey", "Test string");

        HashSet<String> fieldsToCleanup = new HashSet<>(map.keySet());
        HashSet<String> fieldsToOutput = new HashSet<>();

        App.cleanup(map, fieldsToCleanup, fieldsToOutput);

        assertEquals(map.size(), 0);
    }
}
