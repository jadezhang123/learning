package own.jadezhang.common.utils;

import java.util.UUID;

/**
 * Created by Zhang Junwei on 2016/11/2.
 */
public class CommonUtil {
    /**
     * uuid 32位
     * @return
     */
    public static String makeUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
