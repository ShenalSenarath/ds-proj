package lk.ac.mrt.cse.dsproj;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by shenal on 1/4/16.
 */
public class Node implements Runnable {
    private InetAddress nodeIP;
    private int nodePort;
    private ArrayList<Node> peers;
    private ArrayList<Node> neighbours;
    private String[] fileList;

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
    }

    public Node(int nodePort) throws UnknownHostException {
        this.nodePort = nodePort;
        this.nodeIP= InetAddress.getLocalHost();
        peers = new ArrayList<Node>();
        neighbours= new ArrayList<Node>();
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeIP=" + nodeIP.getHostAddress() +
                ", nodePort=" + nodePort +
                '}';
    }

    public Node(InetAddress nodeIP, int nodePort) {
        this.nodeIP = nodeIP;
        this.nodePort = nodePort;
    }

    public InetAddress getNodeIP() {
        return nodeIP;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void addPeer(Node peer){
        peers.add(peer);
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public ArrayList<Node> getPeers() {
        return peers;
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    @Override
    public void run()  {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(this.nodePort);
        } catch (SocketException e) {
            System.out.println("Cannot initiate the UDP server for the Node.");
        }

        byte[] receiveData = new byte[256];

        while(true)
            try{
                //TODO: Implement it correctly
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();

                //str = sentence.substring(5,8); JOI
                String [] resultArr = sentence.split(" ");
                //System.out.println(resultArr[1]);

                switch (resultArr[1]){
                    case "JOIN":
                        System.out.println("Join request found. Passing to handler");
                        JoinReqHandler jh = new JoinReqHandler(sentence, this);
                        jh.start();
                        break;

                    case "JOINOK":
                        if(resultArr[2].trim().equals("0000")){
                            System.out.println("JOIN request successful");
                        }else{
                            if(resultArr[2].trim().equals("9999")){
                                System.out.println("JOIN request failed");
                            }else{
                                System.out.println("JOIN request failed with UNKNOWN ERROR");
                            }
                        }
                        break;

                    case "LEAVE":
                        System.out.println("LEAVE: not yet implemented");
                        break;

                    case "LEAVEOK":
                        if(resultArr[2].trim().equals("0000")){
                            System.out.println("LEAVE request successful");
                        }else{
                            if(resultArr[2].trim().equals("9999")){
                                System.out.println("LEAVE request failed");
                            }else{
                                System.out.println("LEAVE request failed with UNKNOWN ERROR");
                            }
                        }
                        break;

                    case "SER":
                        System.out.println("Search request found. Passing to handler");
                        SearchReqHandler sh = new SearchReqHandler(sentence, this);
                        sh.start();
                        break;

                    default:
                        System.out.println("Invalid Message");
                }
            } catch (Exception e ){
                e.printStackTrace();
            }
    }

    public void joinNeighbours() {
        int numberOfPeers=peers.size();

        if(numberOfPeers==0){//When there are no peers in the system
            System.out.println("No peers to connect");
        }else if(numberOfPeers<4){//When there are less than 4 nodes
            for (Node peer : peers) {
                try {
                    joinNeighbour(peer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{//When there are more nodes in the system
            int randomIndex1=(int)(Math.random()*1000)%numberOfPeers;
            int randomIndex2;
            do{
                randomIndex2=(int)(Math.random()*1000)%numberOfPeers;
            }while(randomIndex1==randomIndex2);

            try {
                joinNeighbour(peers.get(randomIndex1));
                joinNeighbour(peers.get(randomIndex2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String sendMessage(InetAddress ip, int port, String msg) throws IOException {
        String nodeResponse;
        System.out.println("TO Node: "+ msg);
        DatagramPacket packet;
        DatagramSocket socket = new DatagramSocket();

        byte[] sendBuf = new byte[msg.length()];
        sendBuf = msg.getBytes();

        packet = new DatagramPacket(sendBuf, sendBuf.length, ip, port);
        socket.send(packet);

        byte[] receiveBuf = new byte[16];
        packet = new DatagramPacket(receiveBuf, receiveBuf.length);
        socket.receive(packet);

        nodeResponse = new String(packet.getData(), 0, packet.getLength());
        System.out.println("From Node: "+ nodeResponse);
        return nodeResponse;
    }

    private boolean joinNeighbour(Node node) throws IOException {
        System.out.println("Joining with node" + node);

        /*
        length JOIN IP_address port_no
        e.g., 0027 JOIN 64.12.123.190 432
        length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
        JOIN – Join request.
        IP_address – IP address in xxx.xxx.xxx.xxx format. This is the IP address other nodes will use to reach you.
        Indicated with up to 15 characters.
        port_no – Port number. This is the port number that other nodes will connect to. Up to 5 characters.
         */

        //TODO: implement method to join
        String msg=" JOIN "+ this.getNodeIP().getHostAddress()+" "+ this.getNodePort()+" ";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);

        String result = sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);

        String [] resultArr=result.split(" ");
        if (!resultArr[1].equals("JOINOK") || (Integer.parseInt(resultArr[2]) >= 9999)) {
            return false;
        }
        addNeighbour(node);
        return true;
    }

    public void searchFile(Node node, String file, int hops){
        //search here
        //search in neighbours
        //go by reducing hop count;
    }
}