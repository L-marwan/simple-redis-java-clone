package com.marouane.challenges.redisclone;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RedisProtocolParser {
    private static Logger logger = Logger.getLogger(RedisProtocolParser.class.getName());

    public static Object parse(InputStream is) throws IOException {

        Reader reader = new BufferedReader(new InputStreamReader
                (is, StandardCharsets.UTF_8));

        return parse(reader);
    }

    private static Object parse(Reader reader) throws IOException {
        int c = 0;
        char ch = (char) reader.read();
        return switch (ch) {
            case '+' -> parseSimpleString(reader);
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
            result.add(parse(reader));
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
}
