package test;

/**
 * Created by Zhang Junwei on 2016/10/25.
 */
public abstract class TimingTester {

    public void execute(){
        long startTime = System.currentTimeMillis();
        run();
        long endTime = System.currentTimeMillis();
        System.out.println("this method executed "+ (endTime - startTime) +"ms");
    }

    public abstract void run();
}
