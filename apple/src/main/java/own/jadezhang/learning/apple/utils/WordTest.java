package own.jadezhang.learning.apple.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.FileInputStream;

/**
 * Created by Zhang Junwei on 2016/12/22 0022.
 */
public class WordTest {
    public static void main(String[] args) throws Exception {
        /*WordUtils wordUtils = new WordUtils();
        //XWPFParagraph paragraph = wordUtils.createParagraph();

        wordUtils.createTable(2,3);
        wordUtils.saveDocument("D:\\doc.doc");

        XWPFDocument document = new XWPFDocument(new FileInputStream("D:\\学生体质健康记录卡.doc"));
        List<XWPFParagraph> paragraphs = document.getParagraphs();*/

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        WordUtil.CTRStyleBuilder ctrStyleBuilder = new WordUtil.CTRStyleBuilder();
        XWPFRun run1 = paragraph.createRun();
        System.out.println(run1.getCTR());
        ctrStyleBuilder.init(run1).content("阿斯蒂芬好难过11").font("宋体","Times New Roman","20");
        System.out.println(run1.getCTR());
        CTRPr rPr = run1.getCTR().getRPr();
        System.out.println(rPr);

        XWPFParagraph paragraph1 = document.createParagraph();
        ctrStyleBuilder.init(paragraph1.createRun()).content("阿斯蒂芬\n不难过").samePrOf(run1).bold(true);

        WordUtil.saveDocument(document, "D:\\doc.doc");
    }
}
