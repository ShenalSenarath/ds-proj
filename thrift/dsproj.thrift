namespace java lk.ac.mrt.cse.dsproj.rpc

typedef string String
typedef i32 int

service NodeService {  
        String join(1:String requesterIP, 2:int requesterPort),
        void search(1:String keyWord, 2:String requesterIP, 3:String requesterPort, 4:int hops),
        void sendResult( 1:String senderIP, 2:String senderPort, 3:String resultFiles),
        void leave(1:String senderIP, 2:String senderPort)
}
