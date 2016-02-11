package lk.ac.mrt.cse.dsproj;

import lk.ac.mrt.cse.dsproj.rpc.NodeService;
import lk.ac.mrt.cse.dsproj.rpc.NodeServiceImpl;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by shenal on 1/4/16.
 */
public class Node implements Runnable {
    private InetAddress nodeIP;
    private int nodePort;
    private ArrayList<Node> peers;
    private ArrayList<Node> neighbours;
    private String[] fileList;
    public NodePage nodePage;

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
        this.printFIleList(fileList);
       
    }

    public Node(int nodePort) throws UnknownHostException {
        this.nodePort = nodePort;
        this.nodeIP= getLocalAddress();
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
    
    public  void    addFile(String fileName){
        String[] files= new String[this.fileList.length+1];
        for(int i=0;i<fileList.length;i++){
            files[i]=fileList[i];
        }
        files[this.fileList.length]=fileName;
        this.fileList =files;
        this.printFIleList(files);
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
        return (ArrayList<Node>) neighbours.clone();
    }

    public void show(){
        nodePage = new NodePage(this);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                nodePage.setVisible(true);
            }
        });

    }
    @Override
    public void run()  {
        TNonblockingServerTransport serverTransport = null;
        try {
            serverTransport = new TNonblockingServerSocket(this.getNodePort());
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        NodeService.Processor processor = new NodeService.Processor(new NodeServiceImpl(this));

        TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).
                processor(processor));
        System.out.println("Starting server on port "+ getNodePort());
        server.serve();
        System.out.println("Started server on port "+ getNodePort());
        this.joinNeighbours();
        nodePage = new NodePage(this);
        nodePage.setVisible(true);
          

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

    public void initiateSearch(String file_name, int hops) throws IOException{
        System.out.println("Initiating search query.");
        //length SER IP port file_name hops

        SearchQuery query = new SearchQuery(this, file_name, hops);
        String result = this.searchFileList(query);

        if(result.equals("")) { //returned empty string
            //this node does not have any matching files
            //propagating to neighbours
            System.out.println("No matching files found. Propagating request to neighbours");
            ArrayList<Node> neighbours = this.getNeighbours();
            query.decrementHopsLeft();
            while(!neighbours.isEmpty() && query.hopsLeft()){
                Node n = neighbours.remove(0);
                try{
                    this.propagateSearchQuery(n, query);
                }catch (IOException e){
                    System.out.println("Error while propagating search query");
                    e.printStackTrace();
                    System.exit(1);
                }
                query.decrementHopsLeft();
            }
        }else{
            try{
                this.respondToSearch(query.getRequester(), result, query.getHopLimit() - query.getHopsLeft());
            } catch (IOException e){
                System.out.println("Error while calling respond to search from mainNode");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void initiateSearch(String file_name) throws IOException{
        System.out.println("Initiating search query.");
        //length SER IP port file_name hops

        SearchQuery query = new SearchQuery(this, file_name);
        String result = this.searchFileList(query);

        if(result.equals("")) { //returned empty string
            //this node does not have any matching files
            //propagating to neighbours
            System.out.println("No matching files found. Propagating request to neighbours");
            ArrayList<Node> neighbours = this.getNeighbours();
            query.decrementHopsLeft();
            while(!neighbours.isEmpty() && query.hopsLeft()){
                Node n = neighbours.remove(0);
                try{
                    this.propagateSearchQuery(n, query);
                }catch (IOException e){
                    System.out.println("Error while propagating search query");
                    e.printStackTrace();
                    System.exit(1);
                }
                query.decrementHopsLeft();
            }
        }else{
            try{
                this.respondToSearch(query.getRequester(), result, query.getHopLimit() - query.getHopsLeft());
            } catch (IOException e){
                System.out.println("Error while calling respond to search from mainNode");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void propagateSearchQuery(Node node, SearchQuery sq) throws IOException{
        System.out.println("Propagating search query to node" + node);

        TTransport transport;
        try {
            transport = new TFramedTransport(new TSocket(""+node.getNodeIP().getHostAddress(), node.getNodePort()));
            TProtocol protocol = new TBinaryProtocol(transport);

            NodeService.Client client = new NodeService.Client(protocol);
            transport.open();

            client.search(sq.getSearchStringFull(),sq.getRequester().toString(),""+sq.getRequester().getNodePort(),sq.getHopsLeft());


            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }

        /*
        //length SER IP port file_name hops

        String msg=" SER "+ this.getNodeIP().getHostAddress()+" "+ this.getNodePort()+" ";
        msg = msg + "\""+ sq.getSearchStringFull() + "\" " + sq.getHopsLeft() + " ";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        */
    }

    private void joinNeighbour(Node node) throws IOException {
        System.out.println("Joining with node" + node);
        //length JOIN IP_address port_no

        TTransport transport;
        try {
            transport = new TFramedTransport(new TSocket(""+node.getNodeIP().getHostAddress(), node.getNodePort()));
            TProtocol protocol = new TBinaryProtocol(transport);

            NodeService.Client client = new NodeService.Client(protocol);
            transport.open();

            String result = client.join(""+getNodeIP().getHostAddress(), getNodePort());

            if(result.equals("JOIN OK")){
                System.out.println("Joined with node" + node);
                this.addNeighbour(node);
            }


            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
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
    private static InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
            }
            return InetAddress.getLocalHost();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}