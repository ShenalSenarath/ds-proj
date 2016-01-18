package lk.ac.mrt.cse.dsproj;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rajind on 1/12/16.
 */
public class SearchReqHandler extends Thread{
    private String requestMessage;
    private Node mainNode;
    private SearchQuery query;

    public SearchReqHandler(String sentence, Node n) {
        requestMessage = sentence;
        mainNode = n;
    }

    @Override
    public void run() {
        //length SER IP port file_name hops
        query = new SearchQuery(requestMessage);


        //mainNode.searchFile(node, fileName, hops);
    }
}