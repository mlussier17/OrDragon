/**
 * Created by 196128636 on 2016-05-13.
 */
public class JobThread implements Runnable {
    private Job job;
    private PlayerThread pThread;

    public JobThread(Job job_, PlayerThread pt) {
        pThread = pt;
        job = job_;
    }

    public void run() {
        pThread.jobs.add(job);
        while(!job.isDone()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie){
                // todo
            }
        }
    }
}
