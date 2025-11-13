package com.example.tournamentsystembyt.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Can you write universal method that will create file and store all of the information.
 * I need to have several files that will be created accordingly to the class name.
 * I need to save all of the information in json format.
 * Also I need to have method that will load information from the file and create objects accordingly.
 * Do not use any external libraries.
 */

public class ExtentPersistance {
    public static <T> void saveExtent(Class<T> clazz, List<T> extent) {
        String fileName = clazz.getSimpleName() + "_extent.json";

        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < extent.size(); i++) {
            T obj = extent.get(i);
            json.append(objectToJson(obj));

            if (i < extent.size() - 1) {
                json.append(",\n");
            }
        }

        json.append("\n]");

        try {
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of(fileName),
                    json.toString());
            System.out.println("Extent saved -> " + fileName);
        } catch (Exception e) {
            System.out.println("Saving extent failed: " + e.getMessage());
        }
    }

    public static <T> List<T> loadExtent(Class<T> clazz) {
        String fileName = clazz.getSimpleName() + "_extent.json";

        try {
            java.nio.file.Path path = java.nio.file.Path.of(fileName);

            if (!java.nio.file.Files.exists(path)) {
                System.out.println("No extent file for " + clazz.getSimpleName());
                return List.of();
            }

            String json = java.nio.file.Files.readString(path);

            return parseJsonArray(json, clazz);

        } catch (Exception e) {
            System.out.println("Loading extent failed: " + e.getMessage());
            return List.of();
        }
    }

    private static String objectToJson(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        var fields = obj.getClass().getDeclaredFields();

        int count = 0;

        for (var field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                sb.append("\"")
                        .append(field.getName())
                        .append("\": ");

                if (value == null) {
                    sb.append("null");
                } else if (value instanceof Number || value instanceof Boolean) {
                    sb.append(value);
                } else {
                    sb.append("\"").append(value.toString()).append("\"");
                }

                if (count < fields.length - 1)
                    sb.append(", ");
            } catch (Exception ignored) {
            }

            count++;
        }
        sb.append("}");
        return sb.toString();
    }

    private static <T> List<T> parseJsonArray(String json, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        // Remove brackets
        json = json.trim();
        if (json.startsWith("["))
            json = json.substring(1);
        if (json.endsWith("]"))
            json = json.substring(0, json.length() - 1);

        // Split objects by "},"
        String[] objects = json.split("},\\s*\\{");

        for (String rawObj : objects) {
            String objClean = rawObj.trim();

            if (!objClean.startsWith("{"))
                objClean = "{" + objClean;
            if (!objClean.endsWith("}"))
                objClean = objClean + "}";

            T instance = parseSingleObject(objClean, clazz);
            if (instance != null)
                result.add(instance);
        }

        return result;
    }

    private static <T> T parseSingleObject(String json, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            json = json.substring(1, json.length() - 1); // remove { }
            String[] parts = json.split(",");

            for (String part : parts) {
                String[] kv = part.split(":");
                if (kv.length != 2)
                    continue;

                String key = kv[0].trim().replace("\"", "");
                String value = kv[1].trim().replace("\"", "");

                try {
                    var field = clazz.getDeclaredField(key);
                    field.setAccessible(true);

                    if (value.equals("null")) {
                        field.set(instance, null);
                    } else if (field.getType() == int.class) {
                        field.set(instance, Integer.parseInt(value));
                    } else if (field.getType() == double.class) {
                        field.set(instance, Double.parseDouble(value));
                    } else {
                        field.set(instance, value);
                    }

                } catch (Exception ignored) {
                }
            }

            return instance;

        } catch (Exception e) {
            return null;
        }
    }
}
