package fag.json;

import java.util.List;
import java.util.Map;

public final class JsonWriter {

    private JsonWriter() {
    }

    public static String write(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(value, sb, 0);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static void writeValue(Object value, StringBuilder sb, int indent) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof Map) {
            writeObject((Map<String, Object>) value, sb, indent);
        } else if (value instanceof List) {
            writeArray((List<Object>) value, sb, indent);
        } else if (value instanceof String) {
            writeString((String) value, sb);
        } else if (value instanceof Number) {
            sb.append(formatNumber((Number) value));
        } else if (value instanceof Boolean) {
            sb.append(value.toString());
        } else {
            writeString(String.valueOf(value), sb);
        }
    }

    private static String formatNumber(Number n) {
        double d = n.doubleValue();
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    private static void writeObject(Map<String, Object> map, StringBuilder sb, int indent) {
        if (map.isEmpty()) {
            sb.append("{}");
            return;
        }
        sb.append("{\n");
        int i = 0;
        int size = map.size();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            indent(sb, indent + 1);
            writeString(entry.getKey(), sb);
            sb.append(": ");
            writeValue(entry.getValue(), sb, indent + 1);
            if (++i < size) {
                sb.append(',');
            }
            sb.append('\n');
        }
        indent(sb, indent);
        sb.append('}');
    }

    private static void writeArray(List<Object> list, StringBuilder sb, int indent) {
        if (list.isEmpty()) {
            sb.append("[]");
            return;
        }
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            indent(sb, indent + 1);
            writeValue(list.get(i), sb, indent + 1);
            if (i < list.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        indent(sb, indent);
        sb.append(']');
    }

    private static void writeString(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    private static void indent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
    }
}
