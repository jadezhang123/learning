package own.jadezhang.learning.apple.utils;

import java.util.UUID;

/**
 * Created by Zhang Junwei on 2016/12/29.
 */
public class IDUtil {
    /**
     * uuid 32位
     * @return
     */
    public static String makeUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
