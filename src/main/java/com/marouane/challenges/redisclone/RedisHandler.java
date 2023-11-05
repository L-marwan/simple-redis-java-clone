package com.marouane.challenges.redisclone;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
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
        while (!socket.isClosed()) {
            Object result = RedisProtocolParser.parse(is);
            logger.info("parse result is: " + result);
            if (result instanceof String s && StringUtils.isBlank(s)) break;
            var writer = new PrintWriter(os, true);
            writer.println("+PONG");
        }

        logger.info("Socket closed." + socket);
        return true;
    }
}
