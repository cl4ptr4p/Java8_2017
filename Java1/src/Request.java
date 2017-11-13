
public class Request extends Thread{
    private final int resource;
    private final int time;
    private Server server;

    Request(Server server, String name, int resource, int time){
        this.resource = resource;
        this.time = time;
        this.setName(name);
        this.server = server;
    }

    @Override
    public void run(){
        // Try to get Resource from server
        if(!server.handleRequest(this)){
            return;
        }

        try {
            sleep(getTime());
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + getName() + " can't sleep");
        }

        // Return Resource to server
        server.returnResource(getResource());
        // Note end of request to server logFile
        server.addToLog("INFO: End of " + getName() + "\n");
        //add successful completed request at logFile
        server.addCompleteRequest();
    }

    int getResource() {
        return resource;
    }

    long getTime() {
        return time;
    }

}