package own.jadezhang.learning.apple.utils;

import org.apache.poi.xwpf.usermodel.*;

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
        WordUtil.RunStyleBuilder ctrStyleBuilder = new WordUtil.RunStyleBuilder();
        WordUtil.ParagraphStyleBuilder ctpStyleBuilder = new WordUtil.ParagraphStyleBuilder();
        ctpStyleBuilder.init(paragraph).align(ParagraphAlignment.CENTER, TextAlignment.AUTO)
                .initSpacing().spaceInPound(2, 2);

        XWPFRun run1 = paragraph.createRun();

        ctrStyleBuilder.init(run1).content("阿斯蒂芬好难过11").font("宋体", "Times New Roman", "20");
        XWPFParagraph paragraph1 = document.createParagraph();
        ctpStyleBuilder.init(paragraph1).initSpacing().spaceInLine(2, 3).lineSpace(2, null);
        XWPFRun build = ctrStyleBuilder.init(paragraph1.createRun()).content("阿斯蒂芬\n不难过").samePrOf(run1)
                .bold(true).build();
        ctrStyleBuilder.init(paragraph1.createRun()).samePrOf(build).content("同一段内容");

        XWPFParagraph paragraph2 = document.createParagraph();

        ctpStyleBuilder.init(paragraph2).initInd().indentInChart(2, 0, 0, 0);
        ctrStyleBuilder.init(paragraph2.createRun()).content("第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段").samePrOf(build);
        WordUtil.saveDocument(document, "D:\\doc.doc");
    }
}
