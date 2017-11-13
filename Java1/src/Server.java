
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


 // Main Thread, which have any number of resource and time of work
 // Server prints some notes to the Terminal and to the protocol(logFile)
 // tag INFO: Inform about some good actions
 // tag ERROR: If some action have error
 

public class Server extends Thread{

    private int resource;       // Server current resource
    private int startResource;  // Server initial number of resource
    private long time;          // Time of life server
    private long startTime;     // Time that server was started
    private int requestCounter;          // Count of Requests
    private int completeRequestCounter;  // Count of Complete requests
    private FileWriter log;     // Protocol file
    private String logFileName; // Protocol file name


   
    public Server(String configsFileName){
        System.out.println("INFO: Starting server...\n");
        parseConfigsFile(configsFileName); // Take and set settings from configs file
    }

    @Override
    public void run(){
        new RandomRequestGenerator(this);
        startTime = System.currentTimeMillis(); // Save start time;
        resource = startResource; // Set start requestCounter of resource
        addToLog("INFO: Server started at :" + startTime + "\n");
        // "work" for the time set
        try {
            sleep(getEndTime() - getCurrentTime());
        } catch (InterruptedException e) {
            System.out.println("ERROR: Error in Server work time \n");
        }
        addToLog("INFO: Server finished!\n" + "All requests: " + requestCounter + "  Complete requests: " + completeRequestCounter + "\n");
        System.out.println("INFO: Server off\n");
    }


    //if possible(i.e. has enough resources and time available)
    // handles the request("gives" resources) and returns true
    synchronized boolean handleRequest(Request request) {

        if (getEndTime() - getCurrentTime() < request.getTime()) {
            addToLog("Didn't have time for " + request.getName() + "\n");
            return false;
        }
        if (resource < request.getResource()) {
            addToLog("Didn't have resource for " + request.getName() + "\n");
            return false;
        }
        resource -= request.getResource();
        return true;
    }
    
     // This method opens config file and takes 3 parameters.
     // Lines must go in order: resource, time, log file name
   
    private void parseConfigsFile(String configsFileName) {
        Scanner logScan;
        int resource = 0;
        long time = 0;
        String logFileName = null;
        System.out.println("INFO: trying to set configs...\n");
        try {
            logScan = new Scanner(new File(configsFileName));
        } catch (IOException ex) {
            System.out.println("ERROR: Server can't find file " + configsFileName + "\n" +
                    "using default configs\n");
            setDefaultConfigs(); // Set default value for all settings
            return;
        }

        if(logScan.hasNextLine()){
            resource = Integer.valueOf(logScan.nextLine());
        } else{
            System.out.println("ERROR:  can't find  `resource` \n " +
                    "using default setting\n");
        }

        if(logScan.hasNextLong()){
            time = Long.valueOf(logScan.nextLine());
        } else{
            System.out.println("ERROR:  can't find  `time` \n " +
                    "using default setting\n");
        }

        if(logScan.hasNextLine()){
            logFileName = logScan.nextLine();
        } else{
            System.out.println("ERROR: can't find `logFileName` \n " +
                    "using default setting\n");
        }

        setDefaultConfigs(resource, time, logFileName);
        System.out.println("INFO: Server successfully set configs \n");
    }

    private void setDefaultConfigs() {
        setDefaultConfigs(0, 0, null);
    }
    
    // if parameters could not be found, will set default values
    private void setDefaultConfigs(int resource, long time, String logFileName) {
        // Default values
        final int DEFAULT_RESOURCE = 1000;
        final long DEFAULT_TIME = 2000;
        final String DEFAULT_LOG_FILE_NAME = "log.txt";

        this.startResource = resource == 0 ? DEFAULT_RESOURCE : resource;
        this.time = time == 0 ? DEFAULT_TIME : time;
        this.logFileName = logFileName == null ? DEFAULT_LOG_FILE_NAME : logFileName;

        System.out.println("INFO: Server set settings [res = " + this.startResource
                + " time = " + this.time + " logName = "
                + this.logFileName + "]\n");

        try {
            log = new FileWriter(this.logFileName);
        } catch (IOException e) {
            System.out.println("ERROR: Error occured while creating log file\n");
        }
    }

    // Return resource for server, when request complete his work
    synchronized void returnResource(int res){
        resource += res;
    }

    // Return the resource value at start time server
    int getStartResource() {
        return startResource;
    }

    // Return the time, that server must work
    long getEndTime() {
        return time;
    }

    // Return current time
    long getCurrentTime(){
        return System.currentTimeMillis() - startTime;
    }

    // Increase requestCounter of requests
    //NOTE: this function is only used by one thread (generator), so there is no need to make it synchronised
    int addRequest(){
        return ++requestCounter;
    }
    // Increase requestCounter of completed requests
    synchronized int addCompleteRequest(){
        return ++completeRequestCounter;
    }

    // Write note to protocol
    synchronized void addToLog(String data) {
        try {
            log.write(data);
            log.flush();
        } catch (IOException e) {
            System.out.println("ERROR: Error write to log file\n");
        }
    }

}