package lk.ac.mrt.cse.dsproj;

import lk.ac.mrt.cse.dsproj.communication.BootstrapServer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import static java.net.InetAddress.getByName;
import java.util.LinkedList;

/**
 * Created by shenal on 1/8/16.
 */
public class StartNode {
    
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

    public static void main(String args[]) throws Exception {
//        startPage=new StartPage();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                startPage.setVisible(true);
//            }
//        });
        startNode(args);
    }

    public static String[] getRandomFiles(){
        Random r = new Random();
        //choose randomly between 3,4,5
        int file_count = r.nextInt(3) + 3 ; //random.nextInt(max - min + 1) + min
        String []fileList = new String[file_count];
        List<String> all_files_list = new LinkedList<String>(Arrays.asList(all_files));
        //List<String> all_files_list = Arrays.asList(all_files); //String [] to List

        for( int i=0; i < file_count; i++){
            fileList[i] = all_files_list.remove(r.nextInt(all_files_list.size()-1) );
        }
        return fileList;
    }

    public static void startNode(String args[]){
        int attempt_count = 0;
        try{
            System.out.println("Initializing Node.....");
            int port = Integer.parseInt(args[0]);
            Node thisNode = new Node(port);
            System.out.println("Registering Node with the Bootstrap Server....");

            //bootstrap server ip address and port
            BootstrapServer.startConnection(getByName(args[1]),Integer.parseInt(args[2]));
            BootstrapServer server=BootstrapServer.getInstance();
            boolean success = false;
            System.out.println(args[3]);

            while(attempt_count < 3){
                if(server.registerNode(thisNode, args[3])){
                    System.out.println("Node Successfully registered with the Bootstrap Server. ");
                    success = true;
                    //randomly add fiels
                    thisNode.setFileList(getRandomFiles());
                    //startPage.setVisible(false);
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

            System.out.println("RPC Server for the Node initializing....");
            Thread thread = new Thread(thisNode);
            thread.start();
            Thread.sleep((long) 1000);

            System.out.println("Node joining with neighbours...");
            thisNode.joinNeighbours();
            do {
                System.out.println("Enter\"exit\" to leave");
                System.out.println("Enter name to search:");
                Scanner sc = new Scanner(System.in);
                String filename= sc.nextLine();
                if(filename.equals("exit")){
                    break;
                }
                thisNode.initiateSearch(filename);
            }while (true);

            thisNode.leaveSystem();

            if(server.unregisterNode(thisNode,args[3])){
                System.exit(0);
            }

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