package test;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhang Junwei on 2016/12/20 0020.
 */
public class WordTest {
    private XWPFDocument doc;
    public void readWord(InputStream in) {
        try {
            doc = new XWPFDocument(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        WordTest test = new WordTest();
        test.readWord(new FileInputStream("D:\\学生体质健康记录卡.doc"));


    }

}
