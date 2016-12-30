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

        runBuilder.init(run1).content("标题宋体 20号字体居中显示, 段前段后都是2磅").font("宋体", "Times New Roman", "20");

        //新增一个段前2倍行距段后3倍行距，文本行距2倍行距的段落
        XWPFParagraph firstPar = paragraphBuilder.init(document).initSpacing().spaceInLine(2, 3)
                    .lineSpace(2, null).build();

        XWPFRun build = runBuilder.init(firstPar).content("新的一个段落，段前2倍行距段后3倍行距，文本行距2倍行距的段落，文本加粗，字体和前一个Run相同").samePrOf(run1)
                .bold(true).build();

        runBuilder.init(firstPar, true).samePrOf(build).content("同一段落,但是文本重新换行，并设字体为黑体").font("黑体", "Times New Roman", "40");

        XWPFParagraph paragraph2 = paragraphBuilder.init(document)
                    .initInd().indentInChart(2, 0, 0, 0).build();
        runBuilder.init(paragraph2).content("第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段第二段（首行缩进2个字符）").samePrOf(build);
        wordHolder.saveDocument(document, "D:\\doc.doc");
    }
}
