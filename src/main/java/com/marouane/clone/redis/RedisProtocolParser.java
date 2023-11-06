package com.marouane.clone.redis;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RedisProtocolParser {

    public static ParseResult parseFromRESP(InputStream is) throws IOException {

        Reader reader = new BufferedReader(new InputStreamReader
                (is, StandardCharsets.UTF_8));

        return parseFromRESP(reader);
    }

    private static ParseResult parseFromRESP(Reader reader) throws IOException {
        char ch = (char) reader.read();
        return switch (ch) { // we'll only deal with Strings and arrays for now
            case '$' -> parseBulkString(reader);
            case '*' -> parseArray(reader);
            case '+' -> parseSimpleString(reader);
            default -> parseInline(reader, ch);
        };
    }

    private static ParseResult parseInline(Reader reader, char firstChar) throws IOException {
        String line = ((BufferedReader) reader).readLine();
        if (line == null)
            return new ParseResult(true, "");
        line = firstChar + line;
        return new ParseResult(true, List.of(line.split(" ")));
    }

    private static ParseResult parseArray(Reader reader) throws IOException {
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
            result.add(parseFromRESP(reader).value());
            reader.read(); // skip /r
            reader.read(); // skip /n
            i++;
        }
        return new ParseResult(false, result);
    }

    private static ParseResult parseSimpleString(Reader reader) throws IOException {
        String result = "";
        int c;
        while ((c = reader.read()) != -1 && (char) c != '\r') {
            result += (char) c;
        }
        return new ParseResult(false, result);
    }

    private static ParseResult parseBulkString(Reader reader) throws IOException {
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
        return new ParseResult(false, result);
    }

    public static String parseToRESP(Object o, boolean isInline) {
        String result = "";
        if (o == null) {
            result = "_";
        }
        if (o instanceof String s) {
            result = parseToRESP(s, false);
        }
        if (o instanceof List<?> l) {
            result = parseToRESP(l);
        }
        if (StringUtils.isBlank(result))
            result = "- could not parse result";
        result = isInline ? result : result + "\r\n";
        return result;
    }

    public static String parseToRESP(String s, boolean isBulk) {
        var simpleStringFormat = "+%s";
        var bulkStringFormat = "$%d\r\n%s";
        if (s == null && isBulk) {
            return "$-1";
        }
        return isBulk ? bulkStringFormat.formatted(s.length(), s) : simpleStringFormat.formatted(s);
    }

    public static String parseToRESP(List<?> strings) {
        StringBuilder builder = new StringBuilder();

        builder.append("*")
                .append(strings.size())
                .append("\r\n");
        strings.forEach(s -> builder.append(parseToRESP(s, true)));

        return builder.toString();
    }
}
