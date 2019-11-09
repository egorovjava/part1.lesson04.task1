package part1.lesson04;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Hello world!
 */

public class App {

    public static void main(String[] args) {

    }

    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) {

        if (object == null) {
            return;
        }

        if (fieldsToCleanup == null) {
            fieldsToCleanup = new HashSet<>();
        }

        if (fieldsToOutput == null) {
            fieldsToOutput = new HashSet<>();
        }

        Class<?> cls = object.getClass();

        boolean isImplementsMap = false;
        Class temp = cls;
        while (temp != Object.class) {
            Class[] interfaces = temp.getInterfaces();
            for (Class iface : interfaces) {
                if (iface.equals(Map.class)) {
                    isImplementsMap = true;
                }
            }
            temp = temp.getSuperclass();
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
                for (String field : fieldsToOutput) {
                    try {
                        boolean isContains = (boolean) containsKeyMeth.invoke(object, field);
                        if (isContains) {
                            Object value = getMeth.invoke(object, field);
                            System.out.println(value);
                        } else {
                            throw new IllegalArgumentException("Key " + field + " not found.");
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
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
                    } catch (NoSuchFieldException e) {
                        throw new IllegalArgumentException("Field " + fieldName + " not found.");
                    }

                    Class fieldType = field.getType();
                    Object defValue = null;
                    if (fieldType.isPrimitive()) {
                        switch(fieldType.getName()) {
                            case "boolean":
                                defValue = false;
                                break;
                            case "char":
                                defValue = (char)0;
                                break;
                            default:
                                defValue = 0;
                                break;
                        }
                    }
                    field.set(object, defValue);
                }

                for (String fieldName : fieldsToOutput) {
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
