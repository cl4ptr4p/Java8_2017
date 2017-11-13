
import java.util.Random;


//While server works, generator creates new requests and connects them with server
//Then it sleeps for a little time (from 0 to 100 mls) before creating other requests


public class RandomRequestGenerator implements Runnable {
    private Server server;
    
    
    RandomRequestGenerator(Server server) {
        this.server = server;
        Thread thread;
        thread = new Thread(this, "RandomRequestGenerator");
        thread.start();
    }

    @Override
    public void run(){
        Random random = new Random();
        // Generate new requests, while server has enough time left
        while(server.getEndTime() > server.getCurrentTime()){
            // Generate parameters for a new request
            int res = random.nextInt(server.getStartResource()) + 1;
            int t = random.nextInt((int)server.getEndTime() / 2) + 1;

            Request request = new Request(server, "Request " + server.addRequest(), res, t);
            server.addToLog("Performing Request: " + request.getName() + "\n" +
                    "res: " + request.getResource() + " time: " + request.getTime() + "\n");
            request.start(); // Start request

            // Wait for a random time
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                System.out.println("ERROR: Generator can't wait\n");
            }
        }
        System.out.println("INFO: Finished generating new Requests\n");
    }
}