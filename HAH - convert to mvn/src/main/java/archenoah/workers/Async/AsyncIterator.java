package archenoah.workers.Async;

import java.util.ArrayList;

public class AsyncIterator {

    ArrayList<AsyncTask> queue;
    ArrayList<AsyncJob> running;
    
    public AsyncIterator() {
        queue = new ArrayList<AsyncTask>();
    }
    
    /* **********************/
    
    public void add(AsyncTask task){
        queue.add(task);
    }
    
    public void run(){
        spawnTasks(new AsyncDone() {
            
            @Override
            public void done() {
                
            }
        });
    }
    
    public void run(AsyncDone done){
        spawnTasks(done);
    }
    
    /* **********************/
    
    
    private void spawnTasks(final AsyncDone done){
        running = new ArrayList<AsyncJob>();
        
        for (final AsyncTask task : queue) {
            running.add(new AsyncJob() {
                
                @Override
                public void startJob() {
                    task.task();
                }
                
                @Override
                public void stopJob() {
                    running.remove(this);
                }
            });
        }
        
        new Thread(){
            @Override
            public void run(){
                
                for (AsyncJob job : running) {
                    job.run();
                }
                
                while(running.size() > 0){
                    //keep checking
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TASK Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                done.done();
            }
        }.start();
    }
    
    
    /* **********************/
    
    public abstract static class AsyncTask{
        public abstract void task();
    }
    
    public abstract static class AsyncDone{
        public abstract void done();
    }
    
}
