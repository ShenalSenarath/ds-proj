package lk.ac.mrt.cse.dsproj;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rajind on 1/12/16.
 */
public class JoinReqHandler extends Thread {
    private String requestMessage;
    private Node mainNode;

    public JoinReqHandler(String sentence, Node n) {
        requestMessage = sentence;
        mainNode = n;
    }

    @Override
    public void run() {
        String [] resultArr = requestMessage.split(" ");
        Node node = null;
        try {
            node = new Node(InetAddress.getByName(resultArr[2]),Integer.parseInt(resultArr[3]));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("Accepting request to be added as neighbour");
        mainNode.addNeighbour(node);

        String msg=" JOINOK "+"0000";
        int size = msg.length();
        String formattedSize = String.format("%04d", (size+4));
        String finalMsg=formattedSize.concat(msg);
        try {
            mainNode.sendMessage(node.getNodeIP(), node.getNodePort(), finalMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}