package lk.ac.mrt.cse.dsproj.UDPCommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by shenal on 1/4/16.
 */
public class BootstrapServer {

    private static BootstrapServer instance;
    private String ip;
    private int port;

    public static BootstrapServer getInstance() throws Exception {
        if (instance==null){
            throw new Exception("BootstrapServer has not being initialize");
        }
        return instance;
    }

    public static void startConnection(String ip,int port) throws Exception {
        if (instance !=null){
            throw new Exception("Server is already started.");
        }else {
            instance=new BootstrapServer(ip,port);
        }


    }

    private BootstrapServer(String ip,int port) {
        this.ip=ip;
        this.port=port;
    }

    public String sendMsg(String msg) throws Exception {
        String serverResponse;
        System.out.println("TO Server: "+ msg);
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(msg + '\n');
        serverResponse = inFromServer.readLine();
        System.out.println("FROM SERVER: " + serverResponse);
        clientSocket.close();
        return serverResponse;

    }

}
