package lk.ac.mrt.cse.dsproj.communication;

import lk.ac.mrt.cse.dsproj.Node;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.*;

/**
 * Created by shenal on 1/4/16.
 */
public class BootstrapServer {

    public final static String USERNAME="Shenal1" ;

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
            instance = new BootstrapServer(ip, port);
        }
    }

    private BootstrapServer(InetAddress ip,int port) {
        this.ip = ip;
        this.port = port;
    }

    public String sendMsg(String msg) throws IOException {
        String serverResponse = "not set";
        System.out.println("TO Server: "+ msg);
        Socket clientSocket = null;

        /*
        try(
            Socket clientSocket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        ){
            out.println(msg);
	    //serverResponse = in.readLine();

	    while ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);
            }
        }catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip);
            System.exit(1);
        } catch (IOException e) {
            //System.err.println("Couldn't get I/O for the connection to " + ip);
            System.out.println("Exception caught when trying to listen on port "
                + port + " or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(1);
        }*/

        try{
            clientSocket = new Socket(ip, port);
//            clientSocket.setSoTimeout(10000);
//            clientSocket.setKeepAlive(true);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            outToServer.write(msg.getBytes());
            serverResponse = inFromServer.readLine();
        }catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip);
            //System.exit(1);
        } catch (IOException e) {
            //System.err.println("Couldn't get I/O for the connection to " + ip);
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
            System.out.println(e.getMessage());
            //System.exit(1);
        } finally{
            if(clientSocket != null){
                if(!clientSocket.isClosed()){
                    clientSocket.close();
                }
            }
        }

        //clientSocket.close();
        System.out.println("FROM SERVER: " + serverResponse);
        return serverResponse;
    }


    /*Send msg format: length REG IP_address port_no username
    *Receive msg format: length REGOK no_nodes IP_1 port_1 IP_2 port_2
    */
    public boolean registerNode(Node node, String usr_name) throws IOException {
        String msg=" REG "+ node.getNodeIP().getHostAddress()+" "+node.getNodePort()+" "+usr_name;
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg = formattedSize.concat(msg);
        System.out.println("regMSG : "+finalMsg);
        String result = sendMsg(finalMsg);// Expected Format: length REGOK numberOfNodes ip1 port1 ip2 port2
        System.out.println("result : "+result);
        String [] resultArr=result.split(" ");
        if (!resultArr[1].equals("REGOK") || (Integer.parseInt(resultArr[2])>=99996)) {
            return false;
        }

        int numberOfNodes=Integer.parseInt(resultArr[2]);
        for (int i = 0; i < numberOfNodes ; i++) {
            Node currentPeerNode= new Node(InetAddress.getByName(resultArr[3*i+3]),Integer.parseInt(resultArr[3*i+4]));
            node.addPeer(currentPeerNode);
        }
        return true;
    }

    public boolean unregisterNode(Node node, String usr_name) throws IOException {
        String msg=" UNREG "+ node.getNodeIP().getHostAddress()+" "+node.getNodePort()+" "+usr_name;
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg = formattedSize.concat(msg);

        String result = sendMsg(finalMsg);// Expected Format: length UNROK value
        String [] resultArr=result.split(" ");

        if (!resultArr[1].equals("REGOK") || !resultArr[2].trim().equals("0000")) {
            return false;
        }
        return true;
    }
}