package org.client;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientTest {

    @org.junit.jupiter.api.Test
    void start() throws IOException {
        Client client = new Client();
        client.settings = "src/test/java/org/client/clientsettings.txt";
        new Thread(client.msgSender);
        new Thread(client.msgReader);

    }

    @org.junit.jupiter.api.Test
    void socketMaker() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1);
        Client client = new Client();
        Socket socket = client.socketMaker("src/test/java/org/client/clientsettings.txt");
        String ss = socket.getLocalAddress().getHostName() + socket.getPort();
        Assertions.assertEquals("localhost1", ss);
        socket.close();
        serverSocket.close();
    }
}