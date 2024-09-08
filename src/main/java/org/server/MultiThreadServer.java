package org.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MultiThreadServer {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(50);
    private final LinkedList<Socket> sockets = new LinkedList<>();
    protected String settings = "src/main/java/org/server/settings.txt";
    ServerLogger serverLogger = new ServerLogger();

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(portReader(settings))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serverLogger.log("SYS", "Server started");
                sockets.add(clientSocket);

                Runnable logic = () -> {
                    try {
                        helloWriter(clientSocket);

                        while (true) {
                            String msg = msgReader(clientSocket);
                            if (msg.contains("exit")) {
                                exitWriter(sockets);
                                serverLogger.log("SYS", "Will be removed socket num" + sockets.indexOf(clientSocket) + ". Thread " + Thread.currentThread().threadId() + " will be interrupt.");
                                clientSocket.close();
                                sockets.remove(clientSocket);
                                Thread.currentThread().interrupt();

                                break;
                            }

                            allWriter(sockets, clientSocket, msg);

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                };
                threadPool.submit(logic);
                serverLogger.log("SYS", "New Thrd was started");
            }
        }
    }


    //методы сервера-пиши при входе, пиши при выходе, пиши всем, читай

    public void helloWriter(Socket clientSocket) throws IOException {
        try (PrintWriter helloWriter = new PrintWriter(clientSocket.getOutputStream(), true)) {
            helloWriter.println("Добро пожаловать  в SuperChat. Ваш текущий ID:" + Thread.currentThread().threadId());
        }
        serverLogger.log("USR", "User ID" + Thread.currentThread().threadId() + " was connected");
    }

    public void exitWriter(LinkedList<Socket> sockets) throws IOException {
        for (Socket socket : sockets) {
            try (PrintWriter exitWriter = new PrintWriter(socket.getOutputStream(), true)) {
                exitWriter.println("Пользователь ID " + Thread.currentThread().threadId() + " покинул чат\n");
            }
            serverLogger.log("USR", "User id" + Thread.currentThread().threadId() + " was left");
        }
    }

    public void allWriter(LinkedList<Socket> sockets, Socket clientSocket, String msg) throws IOException {
        for (Socket socket : sockets) {
            try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true)) {
                if (sockets.size() <= 1) {
                    printWriter.println("В чате пока никого нет, ваше сообщение не будет отправлено\n");
                    serverLogger.log("SYS", "No msg sent because one client");
                } else if (clientSocket != socket) {
                    printWriter.println("|ID" + Thread.currentThread().threadId() + "|" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + msg);
                    serverLogger.log("USR", "Sent msg from Thrd" + Thread.currentThread().threadId() + ":" + msg);
                }
            }

        }
    }

    public String msgReader(Socket socket) throws IOException {
        String inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        serverLogger.log("USR", "Rcvd msg by thrd" + Thread.currentThread().threadId() + ":" + inMsg);
        return inMsg;
    }

    public Integer portReader(String file) throws IOException {
        Integer port;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            port = Integer.parseInt(bufferedReader.readLine());
            serverLogger.log("SYS", "Read port:" + port);
        }
        return port;
    }
}
