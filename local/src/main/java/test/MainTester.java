package test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Created by Zhang Junwei on 2016/11/2.
 */
public class MainTester {

    private String toChinese(String string) {
        String[] s1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] s2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};

        String result = "";

        int n = string.length();
        for (int i = 0; i < n; i++) {

            int num = string.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
            System.out.println("  " + result);
        }

        System.out.println("----------------");
        System.out.println(result);
        return result;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入字符串：");
        String str = scanner.next();
        // 将字符串数字转化为汉字
        MainTester main1 = new MainTester();
        main1.toChinese(str);
       /* GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        for(int i = 0; i<fontName.length ; i++)
        {
            System.out.println(fontName[i]);
        }*/

    }
}
