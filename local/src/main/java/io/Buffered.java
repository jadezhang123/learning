package io;

import proxy.TimingHandler;
import test.Tester;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Zhang Junwei on 2016/10/25.
 */
public class Buffered implements Tester {
    @Override
    public void run() {
        System.out.println("run....");
    }

    public static void main(String[] args) {
        Tester runner = new Buffered();

        InvocationHandler handler = new TimingHandler(runner);

        Tester tester = (Tester) Proxy.newProxyInstance(Buffered.class.getClassLoader(), Buffered.class.getInterfaces(), handler);

        tester.run();
    }
}
