package own.jadezhang.learning.apple.utils;


import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Zhang Junwei on 2016/12/21 0021.
 */
public class WordHolder {
    private XWPFDocument document;

    public WordHolder(){
        document = new XWPFDocument();
    }


    public XWPFParagraph createParagraph() {
        return document.createParagraph();
    }

    /**
     * @Description: 得到段落CTPPr
     */
    public CTPPr getCTPPrOf(XWPFParagraph p) {
        CTPPr pPr = null;

        return pPr;
    }

    /**
     * @Description: 保存文档
     */
    public void saveDocument(String savePath) throws Exception {
        File file = new File(savePath);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        document.write(fos);
        fos.close();
    }


}
