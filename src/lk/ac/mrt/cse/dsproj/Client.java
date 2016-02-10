package lk.ac.mrt.cse.dsproj;

import lk.ac.mrt.cse.dsproj.rpc.NodeService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by dewmal on 2/8/16.
 */
public class Client {
    private void invoke() {
        TTransport transport;
        try {
            transport = new TSocket("127.0.0.1", 7911);

            TProtocol protocol = new TBinaryProtocol(transport);

            NodeService.Client client = new NodeService.Client(protocol);
            transport.open();

            String addResult = client.join("12", 200);
            System.out.println("Add result: " + addResult);


            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.invoke();

    }
}
