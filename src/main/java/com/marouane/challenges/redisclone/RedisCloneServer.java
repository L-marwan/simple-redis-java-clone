package com.marouane.challenges.redisclone;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RedisCloneServer {
    private static final Logger LOGGER = Logger.getLogger(RedisCloneServer.class.toString());


    public static void main(String[] args) throws IOException {

        int connectionsCounter = 0;
        try (ExecutorService threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor();
             ServerSocket myserverSocket = new ServerSocket(6379)) {

            // getting client request
            while (true) {

                Socket clientSocket = null;

                try {
                    clientSocket = myserverSocket.accept();
                    connectionsCounter++;

                    LOGGER.info("A new connection identified : " + clientSocket);
                    LOGGER.info("Number of connections : " + connectionsCounter);

                    threadPoolExecutor.submit(new RedisHandler(clientSocket));

                } catch (Exception e) {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                    LOGGER.severe(e.getMessage());
                }
            }
        }
    }
}
