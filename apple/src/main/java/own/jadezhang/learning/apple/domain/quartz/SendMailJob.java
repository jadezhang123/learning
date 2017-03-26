package own.jadezhang.learning.apple.domain.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Zhang Junwei on 2017/3/26.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SendMailJob extends QuartzJobBean {
    private String sender;
    private String receiver;
    private String content;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println(sender + " send a mail to " + receiver + "; the content is " + content);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(receiver + " received a mail from " + sender + "; the content is " + content);
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
