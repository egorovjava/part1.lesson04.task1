package part1.lesson04;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        HashSet<String> fieldsToCleanup = new HashSet<>();
        HashSet<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("intField");
        fieldsToOutput.add("boolField");
        fieldsToOutput.add("charField");
        fieldsToOutput.add("strField");
        TestClass testClass = new TestClass();

        cleanup(testClass, fieldsToCleanup, fieldsToOutput);
        fieldsToCleanup.add("intField");
        fieldsToCleanup.add("boolField");
        fieldsToCleanup.add("charField");
        fieldsToCleanup.add("strField");
        cleanup(testClass, fieldsToCleanup, fieldsToOutput);

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "One");
        map.put("2", "Two");
        map.put("3", "Three");
        map.put("4", "Four");

        fieldsToCleanup.clear();
        fieldsToOutput.clear();

        fieldsToOutput.add("1");
        fieldsToOutput.add("2");
        fieldsToOutput.add("3");
        fieldsToOutput.add("4");

        cleanup(map, fieldsToCleanup, fieldsToOutput);

        fieldsToCleanup.add("1");
        fieldsToCleanup.add("2");
        fieldsToCleanup.add("3");
        fieldsToCleanup.add("4");

        //cleanup(map, fieldsToCleanup, fieldsToOutput);
    }

    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) {

        if(object == null) {
            return;
        }

        if(fieldsToCleanup == null) {
            fieldsToCleanup = new HashSet<>();
        }

        if(fieldsToOutput == null) {
            fieldsToOutput = new HashSet<>();
        }

        Class<?> cls = object.getClass();

        Class[] interfaces = cls.getInterfaces();
        boolean isImplementsMap = false;

        for(Class iface : interfaces) {
            if(iface.equals(Map.class)) {
                isImplementsMap = true;
                break;
            }
        }

        if (isImplementsMap) {
            try {
                Method containsKeyMeth = cls.getMethod("containsKey", Object.class);
                Method removeMeth = cls.getMethod("remove", Object.class);
                for (String field : fieldsToCleanup) {
                    try {
                        boolean isContains = (boolean) containsKeyMeth.invoke(object, field);
                        if (isContains) {
                            removeMeth.invoke(object, field);
                        } else {
                            throw new IllegalArgumentException("Key " + field + " not found.");
                        }

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println(e);
                    }
                }
                Method getMeth = cls.getMethod("get", Object.class);
                for(String field : fieldsToOutput) {
                    try {
                        boolean isContains = (boolean) containsKeyMeth.invoke(object, field);
                        if(isContains) {
                            Object value = getMeth.invoke(object, field);
                            System.out.println(value);
                        } else {
                            throw new IllegalArgumentException("Key " + field + " not found.");
                        }
                    }
                    catch(IllegalAccessException | InvocationTargetException e) {
                        System.out.println(e);
                    }
                }
            } catch (NoSuchMethodException e) {
                System.out.println(e);
            }
        } else {
            try {
                for (String fieldName : fieldsToCleanup) {
                    Field field;
                    try {
                        field = cls.getField(fieldName);
                        //field = cls.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        throw new IllegalArgumentException("Field " + fieldName + " not found.");
                    }

                    Class fieldType = field.getType();
                    Object defValue = null;
                    if (fieldType.isPrimitive()) {
                        if (fieldType.equals(boolean.class)) {
                            defValue = false;
                        } else if (fieldType.equals(char.class)) {
                            defValue = (char)0;
                        } else {
                            defValue = 0;
                        }
                    }
                    field.set(object, defValue);
                }

                for(String fieldName : fieldsToOutput) {
                    Field field;
                    try {
                        field = cls.getField(fieldName);
                    } catch (NoSuchFieldException e) {
                        throw new IllegalArgumentException("Field " + fieldName + " not found.");
                    }

                    Object value = field.get(object);
                    System.out.println(value);
                }
            } catch (IllegalAccessException e) {
                System.out.println(e);
            }
        }
    }
}

class TestClass {

    public int intField = 100;
    public boolean boolField = true;
    public char charField = 'x';
    public String strField = "Test string";
}
