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
        this.printFIleList(fileList);
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

    public void removeNeighbour(Node neighbour){
        neighbours.remove(neighbour);
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

                String [] resultArr = sentence.split(" ");

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
                        System.out.println("Join request found. Passing to handler");
                        LeaveReqHandler lh = new LeaveReqHandler(sentence, this);
                        lh.start();
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

                    case "SEROK":
                        System.out.println("Search results returned");
                        System.out.println(sentence);
                        break;

                    case "ERROR":
                        //Generic error message, to indicate that a given command is not understood.
                        //For storing and searching files/keys this should be send to the initiator of the message.
                        System.out.println("Generic error. Command is not understood");
                        System.exit(1);
                        break;

                    default:
                        System.out.println("Invalid Message");
                        System.exit(1);
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

    public void leaveSystem() {
        int numberOfNeighbours = neighbours.size();
        if(numberOfNeighbours > 0){ //When there are no neighbours for this node
            for (Node n : neighbours) {
                try {
                    leave(n);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("No neighbours to inform leaving");
        }
    }

    public void sendMessage(InetAddress ip, int port, String msg) throws IOException {
        String nodeResponse;
        System.out.println("TO Node: "+ msg);
        DatagramPacket packet;
        DatagramSocket socket = new DatagramSocket();

        byte[] sendBuf = new byte[msg.length()];
        sendBuf = msg.getBytes();

        packet = new DatagramPacket(sendBuf, sendBuf.length, ip, port);
        socket.send(packet);

	/*
        byte[] receiveBuf = new byte[16];
        packet = new DatagramPacket(receiveBuf, receiveBuf.length);
        socket.receive(packet);

        nodeResponse = new String(packet.getData(), 0, packet.getLength());
        System.out.println("From Node: "+ nodeResponse);
        return nodeResponse;
        */
    }

    public void respondToSearch(Node node, String result, int hops) throws IOException{
        System.out.println("Responding to node" + node);
        //length SEROK no_files IP port hops filename1 filename2 ... ...
        //e.g 0114 SEROK 3 129.82.128.1 2301 baby_go_home.mp3 baby_come_back.mp3 baby.mpeg

        String msg=" SEROK "+ this.getNodeIP().getHostAddress()+" "+ this.getNodePort()+" ";
        msg = msg + hops + " ";

        String []arr = result.split("###");
        for(int i=0; i < arr.length; i++){
            msg = msg + "\""+arr[i]+"\"" + " ";
        }

        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
    }

    public void propagateSearchQuery(Node node, SearchQuery sq) throws IOException{
        System.out.println("Issuing search query to node" + node);
        //length SER IP port file_name hops

        String msg=" SER "+ node.getNodeIP().getHostAddress()+" "+ node.getNodePort()+" ";
        msg = msg + sq.getSearchStringFull() + " " + sq.getHopsLeft() + " ";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
    }

    private void joinNeighbour(Node node) throws IOException {
        System.out.println("Joining with node" + node);
        //length JOIN IP_address port_no

        String msg=" JOIN "+ this.getNodeIP().getHostAddress()+" "+ this.getNodePort()+" ";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        addNeighbour(node);

        /*
        String result = sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        String [] resultArr=result.split(" ");
        if (!resultArr[1].equals("JOINOK") || (Integer.parseInt(resultArr[2]) >= 9999)) {
            return false;
        }
        addNeighbour(node);
        return true;
        */
    }

    public void leave(Node node) throws IOException{
        System.out.println("Node leaving the system. ");
        // length LEAVE IP_address port_no

        String msg=" LEAVE "+ this.getNodeIP().getHostAddress()+" "+ this.getNodePort()+" ";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        removeNeighbour(node);

        /*
        String result = sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        String [] resultArr=result.split(" ");
        if (!resultArr[1].equals("LEAVEOK") || (Integer.parseInt(resultArr[2]) >= 9999)) {
            return false;
        }
        //removeNeighbour(node);
        return true;
        */
    }

    public void printFIleList(String [] files){
        if(files != null){
            System.out.println("File List :");
            for(int i=0; i< files.length; i++){
                System.out.println(files[i]);
            }
        }else{
            System.out.println("File list is not initialized.");
        }
    }

    //Searching for file name in this node
    public String searchFileList(SearchQuery sq){
        String result = "";

        String searchStringFull = sq.getSearchStringFull();
        String []searchString = sq.getSearchString();

        //if(searchString.length == 1){
        for(int i=0; i < fileList.length; i++){
            for(int j=0; j < searchString.length; j++){
                String []fileArr = fileList[i].split(" ");
                for(int k=0; k < fileArr.length; k++){
                    if(searchString[j].equals(fileArr[k])){
                        result = result + fileList[i] + "###";
                    }
                }
            }
        }
        return result;
    }
}