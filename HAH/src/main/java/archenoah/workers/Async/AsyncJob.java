package archenoah.workers.Async;

import com.vaadin.ui.UI;

public abstract class AsyncJob {
    
    public AsyncJob(){
        
    }; 
    
    public abstract void startJob();
    public abstract void stopJob();
    
    public void run(){
        runJob();
    }
    
    private void runJob(){
        new Thread() {
            @Override
            public void run() {
                
                try {
                    startJob();
                    UI.getCurrent().access(new Runnable() {
                        @Override
                        public void run() {
                            stopJob();
                        }
                    });
                } catch (Exception e) {
                    System.out.println("error in AsyncJob");
                    e.printStackTrace();
                }
                
                
            };
        }.start();
    }
    
}
