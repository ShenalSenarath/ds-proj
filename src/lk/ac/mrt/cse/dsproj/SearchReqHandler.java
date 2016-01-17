package lk.ac.mrt.cse.dsproj;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rajind on 1/12/16.
 */
public class SearchReqHandler extends Thread{
    private String requestMessage;
    private Node mainNode;

    public SearchReqHandler(String sentence, Node n) {
        requestMessage = sentence;
        mainNode = n;
    }

    @Override
    public void run() {
        String [] resultArr2 = requestMessage.split(" ");
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(resultArr2[2]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(resultArr2[3]);
        String fileName = resultArr2[4];
        int hops = Integer.parseInt(resultArr2[5]);
        Node node = new Node(ip, port);
        mainNode.searchFile(node, fileName, hops);
    }
}