package com.marouane.challenges.redisclone;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class RedisHandler implements Callable<Boolean> {

    private final InputStream is;
    private final OutputStream os;
    private final Socket socket;

    private final Logger logger;


    public RedisHandler(Socket socket) throws IOException {

        logger = Logger.getLogger("RedisHandler-" + Thread.currentThread().getName());

        // obtaining input and out streams
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.socket = socket;
    }

    @Override
    public Boolean call() throws Exception {

        int i = 0;
        while (!socket.isClosed()) {

            Object result = RedisProtocolParser.parse(is);
            logger.info("parse result is: " + result);
            if (result instanceof String s && StringUtils.isBlank(s)) break;
            var writer = new PrintWriter(os, true);
            writer.println("+PONG");
            i++;
        }

        //logger.info("here" + i++);
        return true;
    }
}
