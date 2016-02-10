package lk.ac.mrt.cse.dsproj.rpc;

import lk.ac.mrt.cse.dsproj.Node;
import lk.ac.mrt.cse.dsproj.StartPage;
import lk.ac.mrt.cse.dsproj.communication.BootstrapServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.net.InetAddress.getByName;
import static org.apache.thrift.server.TNonblockingServer.*;

/**
 * Created by dewmal on 2/8/16.
 */
public class Server {

    private void start(Node node) {
        try {
            node.joinNeighbours();
            Thread thread = new Thread(node);
            thread.start();
            Thread.sleep((long)1000);
            //node.show();
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(node.getNodePort());
            NodeService.Processor processor = new NodeService.Processor(new NodeServiceImpl(node));

            TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).
                    processor(processor));
            System.out.println("Starting server on port "+node.getNodePort());
            server.serve();

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startPage=new StartPage(new Server());
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                startPage.setVisible(true);
            }
        });
//        Server srv = new Server();
//        srv.start();
    }

    private static StartPage startPage;
    //Need to implement methods to read files names and queries and add it to a list.
    private static String[] all_files = {
            "Adventures of Tintin",
            "Jack and Jill",
            "Glee",
            "The Vampire Diarie",
            "King Arthur",
            "Windows XP",
            "Harry Potter",
            "Kung Fu Panda",
            "Lady Gaga",
            "Twilight",
            "Windows 8",
            "Mission Impossible",
            "Turn Up The Music",
            "Super Mario",
            "American Pickers",
            "Microsoft Office 2010",
            "Happy Feet",
            "Modern Family",
            "American Idol",
            "Hacking for Dummies"	};


    public static String[] getRandomFiles(){
        Random r = new Random();
        //choose randomly between 3,4,5
        int file_count = r.nextInt(3) + 3 ; //random.nextInt(max - min + 1) + min
        String []fileList = new String[file_count];
        List<String> all_files_list = new LinkedList<String>(Arrays.asList(all_files));

        for( int i=0; i < file_count; i++){
            fileList[i] = all_files_list.remove(r.nextInt(all_files_list.size()-1) );
        }
        return fileList;
    }

    public void startNode(String args[]){
        int attempt_count = 0;
        try{
            System.out.println("Initializing Node.....");
            int port = Integer.parseInt(args[0]);
            Node thisNode = new Node(port);
            System.out.println("Registering Node with the Bootstrap Server....");

            //bootstrap server ip address and port
            BootstrapServer.startConnection(getByName(args[1]), Integer.parseInt(args[2]));
            BootstrapServer server=BootstrapServer.getInstance();
            boolean success = false;
            System.out.println(args[3]);

            while(attempt_count < 3){
                if(server.registerNode(thisNode, args[3])){
                    System.out.println("Node Successfully registered with the Bootstrap Server. ");
                    success = true;
                    //randomly add fiels
                    thisNode.setFileList(getRandomFiles());
                    startPage.setVisible(false);
                    break;
                }else{
                    Thread.sleep(5000);
                    attempt_count++;
                    System.out.print("Attempt " + attempt_count + " :");
                    System.out.println("Error registering node");
                    //server.unregisterNode(thisNode, args[3]);
                    //need to make sure at what stage we need to unregister. perhaps send some int code relevant to each stage.
                    //e.g if request was successful and only the reading response got issues then we might want to unregister.
                }
            }

            if(!success){
                System.exit(1);
            }
            this.start(thisNode);

//            System.out.println("UDP Server for the Node initializing....");
//            Thread thread = new Thread(thisNode);
//            thread.start();
//            Thread.sleep((long)1000);
//
//            System.out.println("Node joining with neighbours...");
//            thisNode.joinNeighbours();


        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            System.out.println("Please enter the details in the correct format to start the Node.");
            System.out.println("Syntax: java StartNode NodePort BootstrapServerIP BootstrapServerPort ");
            System.out.println("Eg: java StartNode 7001 127.0.0.1 8000 NodeShenal");
            System.exit(10);
        } catch (UnknownHostException e) {
            System.out.println("Cannot determine the host address.");
            System.exit(10);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Network Error.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
