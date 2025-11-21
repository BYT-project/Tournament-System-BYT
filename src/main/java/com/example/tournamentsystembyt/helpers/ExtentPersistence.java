package com.example.tournamentsystembyt.helpers;

/*
 * Universal extent persistence helper.
 * - Creates one JSON file per class: {ClassName}_extent.json
 * - Saves only "simple" attributes (primitives, wrappers, String, LocalDate, LocalTime, java.util.Date, enums)
 * - Skips associations and collections (e.g. Team.players, Match.stage, etc.)
 * - Loads objects using a no-arg constructor and reflection.
 * - No external libraries.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExtentPersistence {

    // -------- PUBLIC API --------

    /**
     * Saves the given extent to a JSON file named {ClassName}_extent.json
     */
    public static <T> boolean saveExtent(Class<T> clazz, List<T> extent) {
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
            Files.writeString(Path.of(fileName), json.toString());
            System.out.println("Extent saved -> " + fileName);
            return true;
        } catch (Exception e) {
            System.out.println("Saving extent failed for " + clazz.getSimpleName() + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads extent from {ClassName}_extent.json and returns it as a list.
     * If file does not exist or is invalid – returns an empty list.
     */
    public static <T> List<T> loadExtent(Class<T> clazz) {
        String fileName = clazz.getSimpleName() + "_extent.json";

        try {
            Path path = Path.of(fileName);

            if (!Files.exists(path)) {
                System.out.println("No extent file for " + clazz.getSimpleName());
                return new ArrayList<>();
            }

            String json = Files.readString(path).trim();
            if (json.isEmpty()) {
                return new ArrayList<>();
            }

            return parseJsonArray(json, clazz);

        } catch (Exception e) {
            System.out.println("Loading extent failed for " + clazz.getSimpleName() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // -------- INTERNAL HELPERS --------

    private static String objectToJson(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        Field[] fields = obj.getClass().getDeclaredFields();
        int written = 0;

        for (Field field : fields) {
            // 1) Skip static fields (like static List<T> extent)
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // 2) Skip non-serializable fields (associations, collections, complex objects)
            if (!isSerializableField(field)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                if (written > 0) {
                    sb.append(", ");
                }

                sb.append("\"").append(field.getName()).append("\": ");
                sb.append(toJsonValue(value));

                written++;
            } catch (Exception ignored) {
                // if one field fails we just skip it
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Decides whether a field should be serialized.
     * We allow only "simple" attribute types:
     *  - primitives
     *  - wrappers (Number, Boolean)
     *  - String
     *  - LocalDate, LocalTime, java.util.Date
     *  - enums
     * Everything else (List, associations, complex objects) is skipped.
     */
    private static boolean isSerializableField(Field field) {
        Class<?> type = field.getType();

        // Skip collections (multi-valued associations, etc.)
        if (List.class.isAssignableFrom(type)) return false;

        // Primitives are OK
        if (type.isPrimitive()) return true;

        // Simple wrappers & String
        if (type == String.class) return true;
        if (Number.class.isAssignableFrom(type)) return true;
        if (type == Boolean.class) return true;

        // Date/time types supported
        if (type == LocalDate.class) return true;
        if (type == LocalTime.class) return true;
        if (type == Date.class) return true;  // <-- java.util.Date

        // Enums
        if (type.isEnum()) return true;

        // Everything else (Ticket, Stage, Team, Player, etc.) is not serialized
        return false;
    }

    private static String toJsonValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }

        if (value instanceof Enum<?>) {
            return "\"" + ((Enum<?>) value).name() + "\"";
        }

        if (value instanceof LocalDate) {
            return "\"" + value.toString() + "\""; // ISO-8601
        }

        if (value instanceof LocalTime) {
            return "\"" + value.toString() + "\""; // HH:mm:ss
        }

        if (value instanceof Date) {
            // store as epoch millis (number, no quotes)
            return String.valueOf(((Date) value).getTime());
        }

        // simple string fallback, escape quotes and backslashes a bit
        String s = value.toString()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        return "\"" + s + "\"";
    }

    private static <T> List<T> parseJsonArray(String json, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        json = json.trim();
        if (json.equals("[]")) {
            return result;
        }

        // Remove outer brackets
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        // Very simple split: assumes no nested objects and no "," inside values
        String[] objects = json.split("},\\s*\\{");

        for (String rawObj : objects) {
            String objClean = rawObj.trim();

            if (!objClean.startsWith("{")) objClean = "{" + objClean;
            if (!objClean.endsWith("}")) objClean = objClean + "}";

            T instance = parseSingleObject(objClean, clazz);
            if (instance != null) {
                result.add(instance);
            }
        }

        return result;
    }

    private static <T> T parseSingleObject(String json, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            // remove { }
            json = json.trim();
            json = json.substring(1, json.length() - 1).trim();
            if (json.isEmpty()) {
                return instance;
            }

            // split "key: value" pairs
            String[] parts = json.split(",");

            for (String part : parts) {
                String[] kv = part.split(":", 2);
                if (kv.length != 2) continue;

                String key = kv[0].trim().replace("\"", "");
                String valueRaw = kv[1].trim();

                Field field;
                try {
                    field = clazz.getDeclaredField(key);
                } catch (NoSuchFieldException e) {
                    continue; // ignore unknown fields
                }

                if (Modifier.isStatic(field.getModifiers())) {
                    continue; // never write to static fields here
                }

                // If field type is not serializable according to our rules, skip it
                if (!isSerializableField(field)) {
                    continue;
                }

                field.setAccessible(true);

                Object parsedValue = parseValueForField(field.getType(), valueRaw);
                field.set(instance, parsedValue);
            }

            return instance;

        } catch (Exception e) {
            System.out.println("Failed to parse object for " + clazz.getSimpleName() + ": " + e.getMessage());
            return null;
        }
    }

    private static Object parseValueForField(Class<?> type, String valueRaw) {
        String value = valueRaw.trim();

        // null
        if (value.equals("null")) {
            if (!type.isPrimitive()) return null;
            // primitive null -> leave default
            return defaultPrimitive(type);
        }

        // strip quotes if present
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            value = value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }

        try {
            if (type == int.class || type == Integer.class) {
                return Integer.parseInt(value);
            }
            if (type == double.class || type == Double.class) {
                return Double.parseDouble(value);
            }
            if (type == long.class || type == Long.class) {
                return Long.parseLong(value);
            }
            if (type == boolean.class || type == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
            if (type == String.class) {
                return value;
            }
            if (type == LocalDate.class) {
                return LocalDate.parse(value);
            }
            if (type == LocalTime.class) {
                return LocalTime.parse(value);
            }
            if (type == Date.class) {
                long epoch = Long.parseLong(value);
                return new Date(epoch);
            }
            if (type.isEnum()) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                Enum e = Enum.valueOf((Class<? extends Enum>) type, value);
                return e;
            }
        } catch (Exception ignored) {
            // if parsing fails, fall through and return null / default
        }

        return type.isPrimitive() ? defaultPrimitive(type) : null;
    }

    private static Object defaultPrimitive(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == double.class) return 0.0d;
        if (type == float.class) return 0.0f;
        if (type == short.class) return (short) 0;
        if (type == byte.class) return (byte) 0;
        if (type == char.class) return '\0';
        return null;
    }
}