package own.jadezhang.learning.apple.utils;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;

/**
 * Created by Zhang Junwei on 2016/12/21 0021.
 */
public class WordUtil {

    public void addNewPage(XWPFDocument document, BreakType breakType) {
        XWPFParagraph p = document.createParagraph();
        p.createRun().addBreak(breakType);
    }

    /**
     * @Description: 设置段落对齐
     */
    public static void setParagraphAlign(XWPFParagraph p, ParagraphAlignment pAlign, TextAlignment vAlign) {
        if (pAlign != null) {
            p.setAlignment(pAlign);
        }
        if (vAlign != null) {
            p.setVerticalAlignment(vAlign);
        }
    }

    public static XWPFRun createRun(XWPFParagraph paragraph, boolean newLine) {
        XWPFRun run = paragraph.createRun();
        if (newLine) {
            run.addBreak();
        }
        return run;
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

    public static CTRPr getPrOfRun(XWPFRun run) {
        CTRPr rPr = run.getCTR().getRPr();
        if (rPr == null) {
            rPr = run.getCTR().addNewRPr();
        }
        return rPr;
    }

    public static class CTRStyleBuilder {
        private XWPFRun run = null;

        public CTRStyleBuilder init(XWPFRun run) {
            this.run = run;
            return this;
        }

        public CTRStyleBuilder content(String content) {
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

        public CTRStyleBuilder bold(boolean bold) {
            run.setBold(bold);
            return this;
        }

        public CTRStyleBuilder italic(boolean italic) {
            run.setItalic(italic);
            return this;
        }

        public CTRStyleBuilder strike(boolean strike) {
            run.setStrike(strike);
            return this;
        }

        public CTRStyleBuilder font(String cnFontFamily, String enFontFamily, String fontSize) {
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

        public CTRStyleBuilder shade(STShd.Enum shdStyle, String shdColor) {
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
        public CTRStyleBuilder position(int position) {
            if (position != 0) {
                run.setTextPosition(position);
            }
            return this;
        }

        public CTRStyleBuilder space(int spacingValue) {
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
        public CTRStyleBuilder verticalAlign(VerticalAlign verticalAlign) {
            if (verticalAlign != null) {
                run.setSubscript(verticalAlign);
            }
            return this;
        }

        public CTRStyleBuilder underLine(STUnderline.Enum underStyle, String underLineColor) {
            CTRPr rPr = getPrOfRun(run);
            CTUnderline udLine = rPr.addNewU();
            udLine.setVal(underStyle);
            udLine.setColor(underLineColor);
            return this;
        }

        public CTRStyleBuilder highLight(STHighlightColor.Enum highStyle) {
            CTRPr rPr = getPrOfRun(run);
            if (highStyle != null) {
                CTHighlight highLight = rPr.isSetHighlight() ? rPr.getHighlight() : rPr.addNewHighlight();
                highLight.setVal(highStyle);
            }
            return this;
        }

        public CTRStyleBuilder samePrOf(CTRPr rPr) {
            if (rPr != null) {
                run.getCTR().setRPr(rPr);
            }
            return this;
        }

        public CTRStyleBuilder samePrOf(XWPFRun otherRun) {
            run.getCTR().setRPr(getPrOfRun(otherRun));
            return this;
        }

        public XWPFRun build() {
            return run;
        }

    }

    public static class CTPStyleBuilder {
        private XWPFParagraph paragraph = null;
        private CTPPr pPr = null;
        private CTSpacing pSpacing = null;
        private CTInd pInd = null;

        public CTPStyleBuilder init(XWPFParagraph paragraph) {
            this.paragraph = paragraph;
            return this;
        }

        public CTPStyleBuilder initSpacing() {
            pPr = getPrOfParagraph(paragraph);
            pSpacing = pPr.getSpacing() != null ? pPr.getSpacing() : pPr.addNewSpacing();
            return this;
        }

        public CTPStyleBuilder beforeSpacing(String before){
            if (pSpacing == null) {
                initSpacing();
            }
            // 段前磅数
            if (before != null) {
                pSpacing.setBefore(new BigInteger(before));
            }
            return this;
        }

        public CTPStyleBuilder afterSpacing(String after){
            if (pSpacing == null) {
                initSpacing();
            }
            // 段后磅数
            if (after != null) {
                pSpacing.setAfter(new BigInteger(after));
            }
            return this;
        }

        public CTPStyleBuilder beforeLines(String beforeLines){
            if (pSpacing == null) {
                initSpacing();
            }
            // 段前行数
            if (beforeLines != null) {
                pSpacing.setBeforeLines(new BigInteger(beforeLines));
            }
            return this;
        }

        public CTPStyleBuilder afterLines(String afterLines){
            if (pSpacing == null) {
                initSpacing();
            }
            // 段前行数
            if (afterLines != null) {
                pSpacing.setAfterLines(new BigInteger(afterLines));
            }
            return this;
        }
    }


}
