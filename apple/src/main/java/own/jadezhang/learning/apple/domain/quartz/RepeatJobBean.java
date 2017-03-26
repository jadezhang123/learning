package own.jadezhang.learning.apple.domain.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Zhang Junwei on 2016/10/19.
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RepeatJobBean extends QuartzJobBean{
    //超时时间
    private int timeout;
    private String logFile = "D:\\log.txt";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("execute......................循环任务");
        try {
            writeLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLog()throws IOException{
        File logFile = new File(this.logFile);
        if (logFile.isDirectory()) {
            throw new IOException("logFile should be a file, not a directory!");
        }
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        RandomAccessFile accessFile = new RandomAccessFile(logFile, "rw");
        accessFile.seek(accessFile.length());
        accessFile.writeUTF("向日志文件中记录一些有用的信息");
        accessFile.close();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
}
