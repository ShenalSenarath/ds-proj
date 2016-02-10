package lk.ac.mrt.cse.dsproj.rpc;

import lk.ac.mrt.cse.dsproj.Node;
import org.apache.thrift.TException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dewmal on 2/8/16.
 */
public class NodeServiceImpl implements NodeService.Iface {
    private Node thisNode;

    public NodeServiceImpl(Node n){

        this.thisNode=n;
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

    }

    @Override
    public void sendResult(String senderIP, String senderPort, String resultFiles) throws TException {

    }
}
