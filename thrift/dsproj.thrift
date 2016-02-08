namespace java lk.ac.mrt.cse.dsproj.rpc

typedef string String
typedef i32 int

service NodeService {  
        String join(1:String myIp, 2:int myPort),
        String search(1:String keyWord, 2:String requestorIP, 3:String requestorPort, 4:int hops),
}
