package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zhang Junwei on 2017/2/23 0023.
 */
public class LogTest {
    final Logger logger  =  LoggerFactory.getLogger(LogTest.class );
    Integer t;
    Integer oldT;
    public   void  setTemperature(Integer temperature)  {
        oldT  =  t;
        t  =  temperature;
        logger.error( " Temperature set to {}. Old temperature was {}. " , t, oldT);
        if  (temperature.intValue()  >   50 )  {
            logger.info( " Temperature has risen above 50 degrees. " );
        }
    }
    public   static   void  main(String[] args)  {
        LogTest wombat  =   new  LogTest();
        wombat.setTemperature( 1 );
        wombat.setTemperature( 55 );
    }
}
