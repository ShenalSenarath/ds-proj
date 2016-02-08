package lk.ac.mrt.cse.dsproj;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.ArrayList;

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

        //search here
        String result = mainNode.searchFileList(query);
        if(result.equals("")){ //returned empty string

            //this node does not have any matching files
            //propagating to neighbours
            System.out.println("No matching files found. Propagating request to neighbours");
            ArrayList<Node> neighbours = (ArrayList<Node>) mainNode.getNeighbours().clone();
            query.decrementHopsLeft();
            while(!neighbours.isEmpty() && query.hopsLeft()){
                Node n = neighbours.remove(0);
                try{
                    mainNode.propagateSearchQuery(n, query);
                }catch (IOException e){
                    System.out.println("Error while propagating search query");
                    e.printStackTrace();
                    System.exit(1);
                }
                query.decrementHopsLeft();
            }
        }else{
            try{
                mainNode.respondToSearch(query.getRequester(), result, query.getHopLimit() - query.getHopsLeft());
            } catch (IOException e){
                System.out.println("Error while calling respond to search from mainNode");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}