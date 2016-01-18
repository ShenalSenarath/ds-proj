package lk.ac.mrt.cse.dsproj;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by shenal on 1/4/16.
 */
public class SearchQuery {
    static final int INITIALHOPS=10;
    private int hops;
    private String searchString;
    private InetAddress senderIP;
    private int senderPort;

	/*
    public SearchQuery(Node senderNode,String searchString) {
        this.senderIP = senderNode.getNodeIP();
        this.senderPort = senderNode.getNodePort();
        this.searchString = searchString;
        this.hops = INITIALHOPS;
    }*/

    //String format: length SER IP port file_name
    public SearchQuery(String UDPData){
        String dataArr[]= UDPData.split(" ");
        try {
            this.senderIP = InetAddress.getByName(dataArr[2]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.senderPort = Integer.parseInt(dataArr[3]);
        this.searchString = dataArr[4];

        if (dataArr.length == 6){
            System.out.println("Optional argument hop count is gives as " + dataArr[5]);
            this.hops = Integer.parseInt(dataArr[5]);
        }else{
            System.out.println("Optional argument hop count is not given. Set to "+ INITIALHOPS);
            this.hops = INITIALHOPS;
        }
    }
}