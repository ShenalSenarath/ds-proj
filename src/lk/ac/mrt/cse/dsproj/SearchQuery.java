package lk.ac.mrt.cse.dsproj;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shenal on 1/4/16.
 */
public class SearchQuery {
    static final int INITIALHOPS=10;
    private int hopLimit;
    private String[] searchString;
    private String searchStringFull;
    private Node requester;
    private int hopsLeft;

    //String format: length SER IP port file_name
    public SearchQuery(String UDPData) {
        String dataArr[] = UDPData.split(" ");
        try {
            requester = new Node(InetAddress.getByName(dataArr[2]),Integer.parseInt(dataArr[3]));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(UDPData);
        if (m.find()) {
            this.searchStringFull = m.group(1);
            this.searchString = searchStringFull.split(" ");
            System.out.println(this.searchStringFull);
        }else{
            System.out.println("Search find name not found");
            System.exit(1);
        }

        if (dataArr.length == 4 + this.searchString.length + 1) {
            System.out.println("Optional argument hop count is gives as " + dataArr[5]);
            this.hopLimit = Integer.parseInt(dataArr[5]);
            this.hopsLeft = hopLimit;
        } else {
            System.out.println("Optional argument hop count is not given. Set to " + INITIALHOPS);
            this.hopLimit = INITIALHOPS;
            this.hopsLeft = INITIALHOPS;
        }
    }

    public SearchQuery(Node node,String searchString) {
        this.requester = node;
        this.searchStringFull = searchString;
        this.searchString = searchString.split(" ");
        this.hopLimit = INITIALHOPS;
        this.hopsLeft = INITIALHOPS;
    }

    public SearchQuery(Node node,String searchString, int hops) {
        this.requester = node;
        this.searchStringFull = searchString;
        this.searchString = searchString.split(" ");
        this.hopLimit = hops;
        this.hopsLeft = hops;
    }

    public int getHopLimit() {
        return hopLimit;
    }

    public String[] getSearchString() {
        return searchString;
    }

    public String getSearchStringFull() {
        return searchStringFull;
    }

    public Node getRequester(){
        return requester;
    }

    public int getHopsLeft(){
        return hopsLeft;
    }

    public void decrementHopsLeft(){
        hopsLeft--;
    }

    public boolean hopsLeft(){
        if(hopsLeft > 0){
            return true;
        }else{
            return false;
        }
    }
}