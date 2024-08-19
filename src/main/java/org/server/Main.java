package org.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MultiThreadServer server = new MultiThreadServer();
        server.start();
    }
}