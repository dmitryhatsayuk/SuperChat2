package org.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class MultiThreadServerTest {

    @Test
    void start() throws InterruptedException {
        MultiThreadServer multiThreadServer = new MultiThreadServer();
        multiThreadServer.settings="src/test/java/org/server/testPortSettings.txt";

        Runnable server =()->{
    try {
        multiThreadServer.start();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
};
Runnable client =()->{
        try (Socket socket = new Socket("localhost", 1)) {
            new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))).println("HELLO");
            new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
         String inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
         Assertions.assertEquals("HELLO",inMsg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
};

new Thread(server).start();
Thread.sleep(2000);
new Thread(client).start();

    }

    @Test
    void helloWriter() {
        long thrN;
        Runnable sending = () -> {
            MultiThreadServer multiThreadServer = new MultiThreadServer();
            LinkedList<Socket> sockets = new LinkedList<>();
            try (ServerSocket serverSocket = new ServerSocket(1)) {
                Socket firstSender = serverSocket.accept();
                Socket secondSender = serverSocket.accept();
                sockets.add(firstSender);
                sockets.add(secondSender);
                multiThreadServer.helloWriter(firstSender);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable receiving = () -> {
            try (Socket socket = new Socket("localhost", 1);Socket anotherSocket = new Socket("localhost",1)) {
                String  allMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                String newMsg = allMsg.substring(0,30);
                Assertions.assertEquals("Добро пожаловать  в SuperChat.", newMsg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread send = new Thread(sending,"send");

        Thread receive = new Thread(receiving);
        send.start();receive.start();

    }

    @Test
    void exitWriter() {
        Runnable sending = () -> {
            MultiThreadServer multiThreadServer = new MultiThreadServer();
            LinkedList<Socket> sockets = new LinkedList<>();
            try (ServerSocket serverSocket = new ServerSocket(1)) {
                Socket firstSender = serverSocket.accept();
                Socket secondSender = serverSocket.accept();
                sockets.add(firstSender);
                sockets.add(secondSender);
                multiThreadServer.exitWriter(sockets);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable receiving = () -> {
            try (Socket socket = new Socket("localhost", 1)) {
                String  allMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                Assertions.assertEquals("Пользователь ID "+Thread.currentThread().threadId()+" покинул чат", allMsg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread send = new Thread(sending);
        Thread receive = new Thread(receiving);
        send.start();receive.start();

    }

    @Test
    void allWriter() {
        Runnable sending = () -> {
            MultiThreadServer multiThreadServer = new MultiThreadServer();
            LinkedList<Socket> sockets = new LinkedList<>();
            try (ServerSocket serverSocket = new ServerSocket(1)) {
                Socket firstSender = serverSocket.accept();
                Socket secondSender = serverSocket.accept();
                sockets.add(firstSender);
                sockets.add(secondSender);
                multiThreadServer.allWriter(sockets,firstSender,"HELLO");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable receiving = () -> {
            try (Socket socket = new Socket("localhost", 1);Socket anotherSocket = new Socket("localhost",1)) {
                String  allMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                Assertions.assertEquals(allMsg, "HELLO");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread send = new Thread(sending);
        Thread receive = new Thread(receiving);
        send.start();receive.start();

    }

    @Test
    void msgReader() throws IOException {
        MultiThreadServer multiThreadServer = new MultiThreadServer();
        String inMsg;
        try (ServerSocket serverSocket = new ServerSocket(1); Socket socketOut = new Socket("localhost", 1)) {
            Socket socketIn = serverSocket.accept();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socketOut.getOutputStream()));
            printWriter.println("HELLO");
            printWriter.flush();
            inMsg = multiThreadServer.msgReader(socketIn);

        }
        Assertions.assertEquals(inMsg, "HELLO");

    }

    @Test
    void portReader() throws IOException {
        MultiThreadServer multiThreadServer = new MultiThreadServer();
        String file = "src/test/java/org/server/testPortSettings.txt";
        Assertions.assertEquals(multiThreadServer.portReader(file), 1);


    }
}