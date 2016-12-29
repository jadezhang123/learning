package own.jadezhang.learning.apple.utils;


import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/12/21 0021.
 */
public class WordUtil {

    public void addNewPage(XWPFDocument document, BreakType breakType) {
        XWPFParagraph p = document.createParagraph();
        p.createRun().addBreak(breakType);
    }

    public static CTPPr getPrOfParagraph(XWPFParagraph p) {
        CTPPr pPr = null;
        if (p.getCTP() != null) {
            if (p.getCTP().getPPr() != null) {
                pPr = p.getCTP().getPPr();
            } else {
                pPr = p.getCTP().addNewPPr();
            }
        }
        return pPr;
    }

    public static CTRPr getPrOfRun(XWPFRun run) {
        CTRPr rPr = run.getCTR().getRPr();
        if (rPr == null) {
            rPr = run.getCTR().addNewRPr();
        }
        return rPr;
    }

    //设置页边距 1厘米约等于567
    public void setDocumentMargin(XWPFDocument document, String left, String top, String right, String bottom) {
        CTSectPr sectPr = document.getDocument().getBody().isSetSectPr() ? document.getDocument().getBody().getSectPr() : document.getDocument().getBody().addNewSectPr();
        CTPageMar ctpagemar = sectPr.addNewPgMar();
        ctpagemar.setLeft(new BigInteger(left));
        ctpagemar.setTop(new BigInteger(top));
        ctpagemar.setRight(new BigInteger(right));
        ctpagemar.setBottom(new BigInteger(bottom));
    }

    /**
     * @Description: 保存文档
     */
    public static void saveDocument(XWPFDocument document, String savePath) throws Exception {
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        document.write(fos);
        fos.close();
    }

    //段落样式构建器
    public static class XWPFParagraphBuilder {

        private static final int PER_LINE = 100;
        private static final int PER_CHART = 100;
        //1厘米≈567
        private static final int PER_CM = 567;
        private static final int PER_POUND = 20;
        private static final int ONE_LINE = 240;

        private XWPFParagraph paragraph = null;
        private CTPPr pPr = null;
        private Map<String, CTPPr> savedPPr = null;
        private CTSpacing pSpacing = null;
        private CTInd pInd = null;

        public XWPFParagraphBuilder init(XWPFDocument document) {
            this.paragraph = document.createParagraph();
            return this;
        }

        public XWPFParagraphBuilder init(XWPFParagraph paragraph) {
            if (paragraph == null){
                throw new IllegalArgumentException("the paragraph should not be null");
            }
            this.paragraph = paragraph;
            return this;
        }

        //设置段落对齐方式
        public XWPFParagraphBuilder align(ParagraphAlignment pAlign, TextAlignment vAlign) {
            ensureInit();
            if (pAlign != null) {
                paragraph.setAlignment(pAlign);
            }
            if (vAlign != null) {
                paragraph.setVerticalAlignment(vAlign);
            }
            return this;
        }

        //初始化段落间距属性，在设置各段落间距前调用
        public XWPFParagraphBuilder initSpacing() {
            ensureInit();
            pPr = getPrOfParagraph(paragraph);
            pSpacing = pPr.getSpacing() != null ? pPr.getSpacing() : pPr.addNewSpacing();
            return this;
        }

        //设置段前和段后间距，以磅为单位
        public XWPFParagraphBuilder spaceInPound(double before, double after) {
            ensureInit();
            if (pSpacing == null) {
                initSpacing();
            }
            pSpacing.setBefore(BigInteger.valueOf((long) (before * PER_POUND)));
            pSpacing.setAfter(BigInteger.valueOf((long) (after * PER_POUND)));
            return this;
        }

        //设置段前和段后间距，以行为单位
        public XWPFParagraphBuilder spaceInLine(double beforeLines, double afterLines) {
            ensureInit();
            if (pSpacing == null) {
                initSpacing();
            }
            pSpacing.setBeforeLines(BigInteger.valueOf((long) (beforeLines * PER_LINE)));
            pSpacing.setAfterLines(BigInteger.valueOf((long) (afterLines * PER_LINE)));
            return this;
        }

        //设置段落行距
        public XWPFParagraphBuilder lineSpace(double value, STLineSpacingRule.Enum spaceRule) {
            ensureInit();
            if (pSpacing == null) {
                initSpacing();
            }
            int unit;
            if (spaceRule == null) {
                spaceRule = STLineSpacingRule.AUTO;
            }
            if (spaceRule.intValue() == STLineSpacingRule.INT_AUTO) {
                //当行距规则为多倍行距时，单位为行，且最小为0.06行
                unit = ONE_LINE;
                if (value < 0.06) {
                    value = 0.06;
                }
            } else {
                //当行距规则为固定值或最小值时，单位为磅，且最小为0.7磅
                unit = PER_POUND;
                if (value < 0.7) {
                    value = 0.7;
                }
            }
            pSpacing.setLine(BigInteger.valueOf((long) (value * unit)));
            pSpacing.setLineRule(spaceRule);
            return this;
        }

        public XWPFParagraphBuilder initInd() {
            ensureInit();
            pPr = getPrOfParagraph(paragraph);
            pInd = pPr.getInd() != null ? pPr.getInd() : pPr.addNewInd();
            return this;
        }

        //设置段落缩进，以厘米为单位; 悬挂缩进高于首行缩进；右侧缩进高于左侧缩进
        public XWPFParagraphBuilder indentInCM(double firstLine, double hanging, double right, double left) {
            ensureInit();
            if (pInd == null) {
                initInd();
            }
            if (firstLine != 0) {
                pInd.setFirstLine(BigInteger.valueOf((long) (firstLine * PER_CM)));
            }
            if (hanging != 0) {
                pInd.setHanging(BigInteger.valueOf((long) (hanging * PER_CM)));
            }
            if (right != 0) {
                pInd.setRight(BigInteger.valueOf((long) (right * PER_CM)));
            }
            if (left != 0) {
                pInd.setLeft(BigInteger.valueOf((long) (left * PER_CM)));
            }
            return this;
        }

        //设置段落缩进，以字符为单位; 悬挂缩进高于首行缩进；右侧缩进高于左侧缩进
        public XWPFParagraphBuilder indentInChart(int firstLine, int hanging, int left, int right) {
            ensureInit();
            if (pInd == null) {
                initInd();
            }
            if (firstLine != 0) {
                pInd.setFirstLineChars(BigInteger.valueOf((long) (firstLine * PER_CHART)));
            }
            if (hanging != 0) {
                pInd.setHangingChars(BigInteger.valueOf((long) (hanging * PER_CHART)));
            }
            if (right != 0) {
                pInd.setRightChars(BigInteger.valueOf((long) (right * PER_CHART)));
            }
            if (left != 0) {
                pInd.setLeftChars(BigInteger.valueOf((long) (left * PER_CHART)));
            }
            return this;
        }

        public XWPFParagraphBuilder savePr(String pPrName){
            if (savedPPr ==null){
                savedPPr = new HashedMap<String, CTPPr>();
            }
            if (pPr != null) {
                savedPPr.put(pPrName, pPr);
            }
            return this;
        }

        public XWPFParagraphBuilder samePrOf(String pPrName) {
            ensureInit();

            if (savedPPr.containsKey(pPrName)){
                return samePrOf(savedPPr.get(pPrName));
            }

            return this;
        }

        public XWPFParagraphBuilder samePrOf(CTPPr pPr) {
            ensureInit();
            if (pPr != null) {
                paragraph.getCTP().setPPr(pPr);
            }
            return this;
        }

        public XWPFParagraphBuilder samePrOf(XWPFParagraph otherPra) {
            ensureInit();
            paragraph.getCTP().setPPr(getPrOfParagraph(otherPra));
            return this;
        }

        public XWPFParagraph build() {
            return paragraph;
        }

        //确保init方法是第一个调用的，避免出现空指针异常
        private void ensureInit(){
            if (this.paragraph == null){
                throw new IllegalStateException("the init method must be invoked firstly");
            }
        }
    }

    //文本样式构建器
    public static class XWPFRunBuilder {
        private XWPFRun run = null;

        public XWPFRunBuilder init(XWPFParagraph paragraph, boolean newLine) {
            this.run = paragraph.createRun();
            if (newLine) {
                run.addBreak();
            }
            return this;
        }

        /**
         * insert a new Run in RunArray
         *
         * @param pos The position at which the new run should be added.
         */
        public XWPFRunBuilder init(XWPFParagraph paragraph, int pos) {
            this.run = paragraph.insertNewRun(pos);
            if (this.run == null){
                return init(paragraph, false);
            }
            return this;
        }

        public XWPFRunBuilder init(XWPFRun run) {
            if (run == null){
                throw new IllegalArgumentException("the run should not be null");
            }
            this.run = run;
            return this;
        }

        public XWPFRunBuilder content(String content) {
            ensureInit();
            if (StringUtils.isNotBlank(content)) {
                // pRun.setText(content);
                if (content.contains("\n")) {// System.properties("line.separator")
                    String[] lines = content.split("\n");
                    run.setText(lines[0], 0); // set first line into XWPFRun
                    for (int i = 1; i < lines.length; i++) {
                        // add break and insert new text
                        run.addBreak();
                        run.setText(lines[i]);
                    }
                } else {
                    run.setText(content, 0);
                }
            }
            return this;
        }

        public XWPFRunBuilder bold(boolean bold) {
            ensureInit();
            run.setBold(bold);
            return this;
        }

        public XWPFRunBuilder italic(boolean italic) {
            ensureInit();
            run.setItalic(italic);
            return this;
        }

        public XWPFRunBuilder strike(boolean strike) {
            ensureInit();
            run.setStrike(strike);
            return this;
        }

        public XWPFRunBuilder font(String cnFontFamily, String enFontFamily, String fontSize) {
            ensureInit();
            CTRPr rPr = getPrOfRun(run);
            // 设置字体
            CTFonts fonts = rPr.isSetRFonts() ? rPr.getRFonts() : rPr.addNewRFonts();
            if (StringUtils.isNotBlank(enFontFamily)) {
                fonts.setAscii(enFontFamily);
                fonts.setHAnsi(enFontFamily);
            }
            if (StringUtils.isNotBlank(cnFontFamily)) {
                fonts.setEastAsia(cnFontFamily);
                fonts.setHint(STHint.EAST_ASIA);
            }
            // 设置字体大小
            CTHpsMeasure sz = rPr.isSetSz() ? rPr.getSz() : rPr.addNewSz();
            sz.setVal(new BigInteger(fontSize));

            CTHpsMeasure szCs = rPr.isSetSzCs() ? rPr.getSzCs() : rPr
                    .addNewSzCs();
            szCs.setVal(new BigInteger(fontSize));
            return this;
        }

        public XWPFRunBuilder shade(STShd.Enum shdStyle, String shdColor) {
            ensureInit();
            CTRPr rPr = getPrOfRun(run);
            // 设置底纹
            CTShd shd = rPr.isSetShd() ? rPr.getShd() : rPr.addNewShd();
            if (shdStyle != null) {
                shd.setVal(shdStyle);
            }
            if (shdColor != null) {
                shd.setColor(shdColor);
                shd.setFill(shdColor);
            }
            return this;
        }

        /**
         * @param position 字符垂直方向上间距位置； >0：提升； <0：降低；=磅值*2
         * @return
         */
        public XWPFRunBuilder position(int position) {
            ensureInit();
            if (position != 0) {
                run.setTextPosition(position);
            }
            return this;
        }

        public XWPFRunBuilder space(int spacingValue) {
            ensureInit();
            if (spacingValue > 0) {
                CTRPr rPr = getPrOfRun(run);
                CTSignedTwipsMeasure measure = rPr.isSetSpacing() ? rPr.getSpacing() : rPr.addNewSpacing();
                measure.setVal(new BigInteger(String.valueOf(spacingValue)));
            }
            return this;
        }

        /**
         * @param verticalAlign SUPERSCRIPT：上标；SUBSCRIPT：下标
         * @return
         */
        public XWPFRunBuilder verticalAlign(VerticalAlign verticalAlign) {
            ensureInit();
            if (verticalAlign != null) {
                run.setSubscript(verticalAlign);
            }
            return this;
        }

        public XWPFRunBuilder underLine(STUnderline.Enum underStyle, String underLineColor) {
            ensureInit();
            CTRPr rPr = getPrOfRun(run);
            CTUnderline udLine = rPr.addNewU();
            udLine.setVal(underStyle);
            udLine.setColor(underLineColor);
            return this;
        }

        public XWPFRunBuilder highLight(STHighlightColor.Enum highStyle) {
            ensureInit();
            CTRPr rPr = getPrOfRun(run);
            if (highStyle != null) {
                CTHighlight highLight = rPr.isSetHighlight() ? rPr.getHighlight() : rPr.addNewHighlight();
                highLight.setVal(highStyle);
            }
            return this;
        }

        public XWPFRunBuilder samePrOf(CTRPr rPr) {
            ensureInit();
            if (rPr != null) {
                run.getCTR().setRPr(rPr);
            }
            return this;
        }

        public XWPFRunBuilder samePrOf(XWPFRun otherRun) {
            ensureInit();
            run.getCTR().setRPr(getPrOfRun(otherRun));
            return this;
        }

        public XWPFRun build() {
            return run;
        }

        private void ensureInit(){
            if (this.run == null){
                throw new IllegalStateException("the init method must be invoked firstly");
            }
        }
    }
}
