package lk.ac.mrt.cse.dsproj.Communication;

import lk.ac.mrt.cse.dsproj.Node;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by shenal on 1/4/16.
 */
public class BootstrapServer {

    public final static String USERNAME="Shenal1234" ;

    private static BootstrapServer instance;
    private InetAddress ip;
    private int port;

    public static BootstrapServer getInstance() throws InstantiationException {
        if (instance==null){
            throw new InstantiationException("BootstrapServer has not initialized");
        }
        return instance;
    }

    public static void startConnection(InetAddress ip,int port) {
        if (instance !=null){
            System.out.println("Server is already started.");
        }else {
            instance=new BootstrapServer(ip,port);
        }


    }

    private BootstrapServer(InetAddress ip,int port) {
        this.ip=ip;
        this.port=port;
    }

    public String sendMsg(String msg) throws IOException {

        String serverResponse;

        System.out.println("TO Server: "+ msg);
        Socket clientSocket = new Socket(ip, port);
        clientSocket.setSoTimeout(10000);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(msg);
        serverResponse = inFromServer.readLine();
        System.out.println("FROM SERVER: " + serverResponse);
        clientSocket.close();
        return serverResponse;

    }
    /*Send msg format: length REG IP_address port_no username
    *Receive msg format: length REGOK no_nodes IP_1 port_1 IP_2 port_2
    */
    public boolean registerNode(Node node) throws IOException {
        String msg=" REG "+node.getNodeIP().getHostAddress()+" "+node.getNodePort()+" "+USERNAME;
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);

        String result =sendMsg(finalMsg);// Expected Format: length REGOK numberOfNodes ip1 port1 ip2 port2
        String [] resultArr=result.split(" ");
        if (!resultArr[1].equals("REGOK") || (Integer.parseInt(resultArr[2])>=9996)) {
            return false;
        }

        int numberOfNodes=Integer.parseInt(resultArr[2]);
        for (int i = 0; i < numberOfNodes ; i++) {
            Node currentPeerNode= new Node(InetAddress.getByName(resultArr[2*i+3]),Integer.parseInt(resultArr[2*i+4]));
            node.addPeer(currentPeerNode);
        }
        return true;

    }

}
