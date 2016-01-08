package lk.ac.mrt.cse.dsproj;

import lk.ac.mrt.cse.dsproj.UDPCommunication.BootstrapServer;

import java.net.InetAddress;

/**
 * Created by shenal on 1/8/16.
 */
public class Main {
    public static void main(String Args[]) throws Exception {
        testServer();

    }

    public static void testServer() throws Exception {
        BootstrapServer.startConnection("localhost",6789);
        BootstrapServer server = BootstrapServer.getInstance();

        server.sendMsg("test");

    }
}
