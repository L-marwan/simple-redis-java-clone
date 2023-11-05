package com.marouane.clone.redis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RedisProtocolParser {

    public static Object parseFromRESP(InputStream is) throws IOException {

        Reader reader = new BufferedReader(new InputStreamReader
                (is, StandardCharsets.UTF_8));

        return parseFromRESP(reader);
    }

    private static Object parseFromRESP(Reader reader) throws IOException {
        char ch = (char) reader.read();
        return switch (ch) { // we'll only deal with Strings and arrays for now
            case '$' -> parseBulkString(reader);
            case '*' -> parseArray(reader);
            default -> parseSimpleString(reader);
        };
    }

    private static List<Object> parseArray(Reader reader) throws IOException {
        String sizeString = "";
        int c;
        List<Object> result = new ArrayList<>();
        while ((c = reader.read()) != -1 && (char) c != '\r') {
            sizeString += (char) c;
        }

        int size = Integer.parseInt(sizeString);
        int i = 0;

        reader.read(); // skip /n
        while (i < size) {
            result.add(parseFromRESP(reader));
            reader.read(); // skip /r
            reader.read(); // skip /n
            i++;
        }
        return result;
    }

    private static String parseSimpleString(Reader reader) throws IOException {
        String result = "";
        int c;
        while ((c = reader.read()) != -1 && (char) c != '\r') {
            result += (char) c;
        }
        return result;
    }

    private static String parseBulkString(Reader reader) throws IOException {
        String sizeString = "";
        String result = "";
        int c;
        while ((c = reader.read()) != -1 && (char) c != '\r') {
            sizeString += (char) c;
        }

        int size = Integer.parseInt(sizeString);
        reader.read(); // skip /n
        char[] chars = new char[size];
        reader.read(chars);
        result = new String(chars);
        return result;
    }

    public static String parseToRESP(Object o) {
        if (o == null) {
            return "_\r\n";
        }
        if (o instanceof String s) {
            return parseToRESP(s, false);
        }
        if (o instanceof List<?> l) {
            return parseToRESP(l);
        }
        return "- could not parse result \r\n";
    }

    public static String parseToRESP(String s, boolean isBulk) {
        var simpleStringFormat = "+%s\r\n";
        var bulkStringFormat = "$%d\r\n%s\r\n";
        if (s == null && isBulk) {
            return "$-1\r\n";
        }
        return isBulk ? bulkStringFormat.formatted(s.length(), s) : simpleStringFormat.formatted(s);
    }

    public static String parseToRESP(List<String> strings) {
        StringBuilder builder = new StringBuilder();

        builder.append("*")
                .append(strings.size())
                .append("\r\n");
        strings.forEach(s -> builder.append(parseToRESP(s, true)));

        return builder.toString();
    }
}
