package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Zhang Junwei on 2016/11/2.
 */
public class MainTester {
    public static void main(String[] args) throws IOException {
        File fi = new File("F:\\document\\杂件\\十九楼平面布置图1027.pdf");
        System.out.println(Files.probeContentType(fi.toPath()));
    }
}
