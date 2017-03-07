package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Zhang Junwei on 2016/10/25.
 */
public class TimingHandler implements InvocationHandler {

    private Object realObject;

    public TimingHandler(Object realObject) {
        this.realObject = realObject;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = method.invoke(realObject, args);
        long endTime = System.currentTimeMillis();
        System.out.println(method.getName() + " executed " + (endTime - startTime) + "ms");
        return result;
    }

}
