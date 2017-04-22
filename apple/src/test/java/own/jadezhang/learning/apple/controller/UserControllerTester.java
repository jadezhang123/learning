package own.jadezhang.learning.apple.controller;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by Zhang Junwei on 2017/4/21 0021.
 */
public class UserControllerTester {
    @Test
    public void name() throws Exception {
        Jedis jedis = new Jedis("192.168.230.128",6379);
        //jedis.auth("04093x");
        System.out.println(jedis.ping());
    }
}
