package com.marouane.clone.redis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class RedisCloneServer {
    private static final Logger LOGGER = Logger.getLogger(RedisCloneServer.class.toString());


    public static void main(String[] args) throws IOException {
        int port = 6379; // default port
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try (ExecutorService threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor();
             ServerSocket myserverSocket = new ServerSocket(port)) {

            // getting client request
            while (true) {
                acceptConnection(myserverSocket, threadPoolExecutor);
            }
        } catch (IOException e) {
            LOGGER.severe("Could not open socket server, cause: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void acceptConnection(ServerSocket myserverSocket, ExecutorService threadPoolExecutor) throws IOException {
        Socket clientSocket = null;
        try {
            clientSocket = myserverSocket.accept();
            LOGGER.info("A new connection identified : %s".formatted(clientSocket));
            threadPoolExecutor.submit(new RedisHandler(clientSocket));

        } catch (Exception e) {
            if (clientSocket != null) {
                clientSocket.close();
            }
            LOGGER.severe(e.getMessage());
        }
    }
}
