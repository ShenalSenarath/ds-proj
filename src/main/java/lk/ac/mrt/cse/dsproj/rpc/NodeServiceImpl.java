package lk.ac.mrt.cse.dsproj.rpc;

import lk.ac.mrt.cse.dsproj.JoinReqHandler;
import lk.ac.mrt.cse.dsproj.Node;
import lk.ac.mrt.cse.dsproj.SearchQuery;
import lk.ac.mrt.cse.dsproj.SearchReqHandler;
import org.apache.thrift.TException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dewmal on 2/8/16.
 */
public class NodeServiceImpl implements NodeService.Iface {
    private Node thisNode;

    public NodeServiceImpl(Node thisNode){

        this.thisNode=thisNode;
    }

    @Override
    public String join(String requesterIP, int requesterPort) throws TException {
        try {
            thisNode.addNeighbour(new Node(InetAddress.getByName(requesterIP),requesterPort));
            return "JOIN OK";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "JOIN ERROR";
        }
    }

    @Override
    public void search(String keyWord, String requesterIP, String requesterPort, int hops) throws TException {
        SearchQuery query= null;
        try {
            query = new SearchQuery(new Node(InetAddress.getByName(requesterIP),Integer.parseInt(requesterPort)),keyWord,hops);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SearchReqHandler searchReqHandler = new SearchReqHandler(query,thisNode);
        searchReqHandler.start();

    }

    @Override
    public void sendResult(String senderIP, String senderPort, String resultFiles) throws TException {
        System.out.println("Search Result"+senderIP+":"+senderPort+" "+resultFiles);
    }

    @Override
    public void leave(String senderIP, String senderPort) throws TException {
        try {
            thisNode.leaveNabor(senderIP,Integer.parseInt(senderPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
