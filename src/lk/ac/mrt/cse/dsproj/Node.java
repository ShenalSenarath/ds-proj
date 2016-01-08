package lk.ac.mrt.cse.dsproj;

import java.net.*;
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
        peers = new ArrayList<>();
        neighbours= new ArrayList<>();
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

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true)
           try{
               //TODO: Implement it correctly
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
              serverSocket.receive(receivePacket);
              String sentence = new String( receivePacket.getData());
              System.out.println("RECEIVED: " + sentence);
              InetAddress IPAddress = receivePacket.getAddress();
              int port = receivePacket.getPort();
              String capitalizedSentence = sentence.toUpperCase();
              sendData = capitalizedSentence.getBytes();
              DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
              serverSocket.send(sendPacket);
           }
           catch (Exception e ){
               e.printStackTrace();
           }
    }

    public void joinNeighbours() {
        int numberOfPeers=peers.size();

        if(numberOfPeers==0){//When there are no peers in the system
            System.out.println("No peers to connect");
        }else if(numberOfPeers<4){//When there are less than 4 nodes
            for (Node peer : peers) {
                joinNeighbour(peer);
            }
        }else{//When there are more nodes in the system
            int randomIndex1=(int)(Math.random()*1000)%numberOfPeers;
            int randomIndex2;
            do{
                randomIndex2=(int)(Math.random()*1000)%numberOfPeers;
            }while(randomIndex1==randomIndex2);

            joinNeighbour(peers.get(randomIndex1));
            joinNeighbour(peers.get(randomIndex2));
        }

    }

    private void joinNeighbour(Node node){
        System.out.println("Joining with node" + node);
        //TODO: implement method to join

        addNeighbour(node);
    }
}
