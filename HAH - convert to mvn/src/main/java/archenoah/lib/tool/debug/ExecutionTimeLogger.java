package archenoah.lib.tool.debug;

public class ExecutionTimeLogger {
    
    private long start_time = 0;
    private long end_time = 0;
    private String message;
    
    public ExecutionTimeLogger(String message) {
        this.message = message;
    }
    
//    
//    resp = GeoLocationService.getLocationByIp(ipAddress);
//    long end_time = System.nanoTime();
//    double difference = (end_time - start_time)/1e6;
    
    public void start(){
        start_time = System.nanoTime();
        printInfo("START", null);
    }
    
    public void stop(){
        
        end_time = System.nanoTime();
        double difference = ((end_time - start_time)/1e6);
        printInfo("STOP", String.valueOf(difference));
    }
    
    private void printInfo(String prefix, String data){
        
        data = (data != null) ? ": " + data : "";
        
        System.out.println(prefix + " " + message + data);
        
    }
    
}
