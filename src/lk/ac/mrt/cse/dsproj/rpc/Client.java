package lk.ac.mrt.cse.dsproj.rpc;

import lk.ac.mrt.cse.dsproj.Node;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;

/**
 * Created by dewmal on 2/8/16.
 */
public class Client {
    private Node thisNode;
    public Client(Node n){
        this.thisNode=n;
    }



    private void joinNeighbour(Node node) throws IOException {
        System.out.println("Joining with node" + node);
        //length JOIN IP_address port_no
        TTransport transport;
        try {
            transport = new TFramedTransport(new TSocket(""+node.getNodeIP(), node.getNodePort()));
            TProtocol protocol = new TBinaryProtocol(transport);

            NodeService.Client client = new NodeService.Client(protocol);
            transport.open();

            String result = client.join(""+thisNode.getNodeIP(), thisNode.getNodePort());

            if(result.equals("JOIN OK")){
                this.thisNode.addNeighbour(node);
            }


            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }


    }

    private void invoke() {
        TTransport transport;
        try {
            transport = new TFramedTransport(new TSocket("localhost", 7911));
            TProtocol protocol = new TBinaryProtocol(transport);

            NodeService.Client client = new NodeService.Client(protocol);
            transport.open();

            String addResult = client.join("ss", 200);
            System.out.println("Add result: " + addResult);

            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }




}
