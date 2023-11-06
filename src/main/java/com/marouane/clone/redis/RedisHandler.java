package com.marouane.clone.redis;

import com.marouane.clone.redis.commands.CommandException;
import com.marouane.clone.redis.commands.CommandHandler;
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
            ParseResult parseResult = null;
            Object result = null;

            parseResult = RedisProtocolParser.parseFromRESP(is);

            if (parseResult == null || (parseResult.value() instanceof String s && StringUtils.isBlank(s)))
                continue;

            logger.info("parse result " + parseResult);
            try {
                result = CommandHandler.handle(parseResult.value());
            } catch (CommandException e) {
                logger.severe("error: " + e.getMessage());
                result = "- ERR " + e.getMessage();
            }

            var writer = new PrintWriter(os, true);

            writer.println(RedisProtocolParser.parseToRESP(result, parseResult.isInline()));
        }

        logger.info("Socket closed. " + socket);
        return true;
    }
}
