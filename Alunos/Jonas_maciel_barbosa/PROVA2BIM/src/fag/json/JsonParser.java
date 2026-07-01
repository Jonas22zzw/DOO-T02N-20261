package fag.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonParser {

    private final String text;
    private int pos;

    private JsonParser(String text) {
        this.text = text;
        this.pos = 0;
    }

    public static Object parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        JsonParser parser = new JsonParser(json);
        parser.skipWhitespace();
        Object value = parser.parseValue();
        parser.skipWhitespace();
        return value;
    }

    private Object parseValue() {
        skipWhitespace();
        if (pos >= text.length()) {
            throw new IllegalStateException("JSON inesperadamente truncado na posicao " + pos);
        }
        char c = text.charAt(pos);
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return parseString();
            case 't':
            case 'f':
                return parseBoolean();
            case 'n':
                parseLiteral("null");
                return null;
            default:
                return parseNumber();
        }
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        expect('{');
        skipWhitespace();
        if (peek() == '}') {
            pos++;
            return map;
        }
        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            char c = text.charAt(pos);
            if (c == ',') {
                pos++;
            } else if (c == '}') {
                pos++;
                break;
            } else {
                throw new IllegalStateException("Esperava ',' ou '}' na posicao " + pos);
            }
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        expect('[');
        skipWhitespace();
        if (peek() == ']') {
            pos++;
            return list;
        }
        while (true) {
            Object value = parseValue();
            list.add(value);
            skipWhitespace();
            char c = text.charAt(pos);
            if (c == ',') {
                pos++;
            } else if (c == ']') {
                pos++;
                break;
            } else {
                throw new IllegalStateException("Esperava ',' ou ']' na posicao " + pos);
            }
        }
        return list;
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = text.charAt(pos++);
            if (c == '"') {
                break;
            }
            if (c == '\\') {
                char esc = text.charAt(pos++);
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        String hex = text.substring(pos, pos + 4);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos += 4;
                        break;
                    default:
                        sb.append(esc);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Double parseNumber() {
        int start = pos;
        while (pos < text.length() && "-+.eE0123456789".indexOf(text.charAt(pos)) >= 0) {
            pos++;
        }
        String numStr = text.substring(start, pos);
        if (numStr.isEmpty()) {
            throw new IllegalStateException("Numero invalido na posicao " + start);
        }
        return Double.parseDouble(numStr);
    }

    private Boolean parseBoolean() {
        if (text.startsWith("true", pos)) {
            pos += 4;
            return Boolean.TRUE;
        } else if (text.startsWith("false", pos)) {
            pos += 5;
            return Boolean.FALSE;
        }
        throw new IllegalStateException("Valor booleano invalido na posicao " + pos);
    }

    private void parseLiteral(String literal) {
        if (!text.startsWith(literal, pos)) {
            throw new IllegalStateException("Esperava literal '" + literal + "' na posicao " + pos);
        }
        pos += literal.length();
    }

    private void skipWhitespace() {
        while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) {
            pos++;
        }
    }

    private char peek() {
        return text.charAt(pos);
    }

    private void expect(char c) {
        skipWhitespace();
        if (pos >= text.length() || text.charAt(pos) != c) {
            throw new IllegalStateException("Esperava '" + c + "' na posicao " + pos);
        }
        pos++;
    }
}
