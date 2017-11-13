

 // args[0] - name of the config file, which contains initial resources, time of work,name of logFile
 
public class Main {
    public static void main(String[] args) {
    
        Server server = new Server(args[0]);
        server.start();
    }

}