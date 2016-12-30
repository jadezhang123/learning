package own.jadezhang.learning.apple.utils;

import org.apache.poi.xwpf.usermodel.*;

/**
 * Created by Zhang Junwei on 2016/12/22 0022.
 */
public class WordTest {
    public static void main(String[] args) throws Exception {

        WordHolder wordHolder = new WordHolder();
        WordHolder.XWPFParagraphBuilder paragraphBuilder = wordHolder.getParagraphBuilder();
        WordHolder.XWPFRunBuilder runBuilder = wordHolder.getRunBuilder();
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = paragraphBuilder.init(document).align(ParagraphAlignment.CENTER, TextAlignment.AUTO)
                .initSpacing().spaceInPound(2, 2).build();

        XWPFRun run1 = paragraph.createRun();

        runBuilder.init(run1).content("阿斯蒂芬好难过11").font("宋体", "Times New Roman", "20");
        XWPFParagraph paragraph1 = document.createParagraph();

        //新增一个段前2倍行距段后3倍行距，文本行距2倍行距的段落
        XWPFParagraph firstPar = paragraphBuilder.init(document).initSpacing().spaceInLine(2, 3)
                    .lineSpace(2, null).build();


        XWPFRun build = runBuilder.init(paragraph1.createRun()).content("阿斯蒂芬\n不难过").samePrOf(run1)
                .bold(true).build();

        runBuilder.init(paragraph1.createRun()).samePrOf(build).content("同一段内容");

        XWPFParagraph paragraph2 = paragraphBuilder.init(document)
                    .initInd().indentInChart(2, 0, 0, 0).build();
        runBuilder.init(paragraph2).content("第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段").samePrOf(build);
        wordHolder.saveDocument(document, "D:\\doc.doc");
    }
}
