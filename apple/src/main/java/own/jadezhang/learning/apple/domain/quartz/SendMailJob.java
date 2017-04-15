package own.jadezhang.learning.apple.domain.quartz;

import org.quartz.*;
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

        setDataFromExecutionContext(context);

        System.out.println(sender + " send a mail to " + receiver + "; the content is " + content);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(receiver + " received a mail from " + sender + "; the content is " + content);
        context.setResult("send email successfully");
    }

    private void setDataFromExecutionContext(JobExecutionContext context){
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        setSender(mergedJobDataMap.getString("sender"));
        setReceiver(mergedJobDataMap.getString("receiver"));
        setContent(mergedJobDataMap.getString("content"));
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
