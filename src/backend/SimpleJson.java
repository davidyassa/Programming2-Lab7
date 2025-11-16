/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimpleJson {

    private SimpleJson() {
    }

    public static Object parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        Parser parser = new Parser(json);
        parser.skipWhitespace();
        Object value = parser.parseValue();
        parser.skipWhitespace();
        return value;
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return quote((String) value);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(quote(entry.getKey()));
                sb.append(":");
                sb.append(stringify(entry.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) value;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(stringify(item));
            }
            sb.append("]");
            return sb.toString();
        }
        return quote(value.toString());
    }

    private static String quote(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    private static final class Parser {

        private final String json;
        private int index;

        Parser(String json) {
            this.json = json;
            this.index = 0;
        }

        void skipWhitespace() {
            while (index < json.length()) {
                char c = json.charAt(index);
                if (Character.isWhitespace(c)) {
                    index++;
                } else {
                    break;
                }
            }
        }

        Object parseValue() {
            skipWhitespace();
            if (index >= json.length()) {
                return null;
            }
            char c = json.charAt(index);
            if (c == '{') {
                return parseObject();
            }
            if (c == '[') {
                return parseArray();
            }
            if (c == '"') {
                return parseString();
            }
            if (c == 't' || c == 'f') {
                return parseBoolean();
            }
            if (c == 'n') {
                return parseNull();
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new HashMap<>();
            index++; // skip {
            skipWhitespace();
            if (index < json.length() && json.charAt(index) == '}') {
                index++;
                return map;
            }
            while (index < json.length()) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                if (index < json.length() && json.charAt(index) == ':') {
                    index++;
                }
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (index < json.length() && json.charAt(index) == ',') {
                    index++;
                    continue;
                }
                if (index < json.length() && json.charAt(index) == '}') {
                    index++;
                    break;
                }
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            index++; // skip [
            skipWhitespace();
            if (index < json.length() && json.charAt(index) == ']') {
                index++;
                return list;
            }
            while (index < json.length()) {
                Object value = parseValue();
                list.add(value);
                skipWhitespace();
                if (index < json.length() && json.charAt(index) == ',') {
                    index++;
                    continue;
                }
                if (index < json.length() && json.charAt(index) == ']') {
                    index++;
                    break;
                }
            }
            return list;
        }

        private String parseString() {
            if (index >= json.length() || json.charAt(index) != '"') {
                return "";
            }
            index++; // skip "
            StringBuilder sb = new StringBuilder();
            while (index < json.length()) {
                char c = json.charAt(index++);
                if (c == '"') {
                    break;
                }
                if (c == '\\' && index < json.length()) {
                    char next = json.charAt(index++);
                    switch (next) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        default:
                            sb.append(next);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private Object parseBoolean() {
            if (json.startsWith("true", index)) {
                index += 4;
                return Boolean.TRUE;
            }
            if (json.startsWith("false", index)) {
                index += 5;
                return Boolean.FALSE;
            }
            return Boolean.FALSE;
        }

        private Object parseNull() {
            if (json.startsWith("null", index)) {
                index += 4;
                return null;
            }
            return null;
        }

        private Number parseNumber() {
            int start = index;
            while (index < json.length()) {
                char c = json.charAt(index);
                if ((c >= '0' && c <= '9') || c == '.' || c == '-' || c == '+'
                        || c == 'e' || c == 'E') {
                    index++;
                } else {
                    break;
                }
            }
            String number = json.substring(start, index);
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                try {
                    return Double.parseDouble(number);
                } catch (NumberFormatException ex) {
                    return 0d;
                }
            }
            try {
                return Long.parseLong(number);
            } catch (NumberFormatException ex) {
                return 0L;
            }
        }
    }
}
