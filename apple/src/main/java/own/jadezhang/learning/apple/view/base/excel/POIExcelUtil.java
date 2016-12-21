package own.jadezhang.learning.apple.view.base.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import own.jadezhang.common.domain.common.ErrorCodeEnum;
import own.jadezhang.common.exception.BizException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Zhang Junwei on 2016/11/22.
 */
public class POIExcelUtil {
    /**
     * 为表格的特定区域绑定下拉框约束
     *
     * @param list   下来框内容
     * @param region 作用区域，长度为4的int数组， region[0]: firstRow, region[1]: lastRow, region[2]: firstCol, region[3]: lastCol
     * @param sheet
     */
    public static void explicitListConstraint(String[] list, int[] region, HSSFSheet sheet) {

        if (region.length != 4) {
            throw new IllegalArgumentException("下拉框区域数据必须设置完全");
        }
        //DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
        CellRangeAddressList cellRegions = new CellRangeAddressList(region[0], region[1], region[2], region[3]);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation dataValidation = new HSSFDataValidation(cellRegions, constraint);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 为单元格添加注释
     *
     * @param content   注释内容
     * @param region    注释矩形框大小；1*1代表一个单元格； region[0] => width; region[1] => height
     * @param patriarch
     * @param cell
     */
    public static void commentForCell(String content, int[] region, HSSFPatriarch patriarch, Cell cell) {
        int col = cell.getAddress().getColumn();
        int row = cell.getAddress().getRow();
        if (region.length != 2) {
            region = new int[]{1, 1};
        }
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) col, row, (short) (col + region[0]), row + region[1]));
        comment.setString(new HSSFRichTextString(content));
        cell.setCellComment(comment);
    }

    /**
     * 将图片插入到指定位置，并设定图片所占区域大小，以单元格为单位
     * @param imgPath
     * @param region  图片位置以及大小；
     *                图片左上角所在单元格 => region[0]：col; region[1]: row;
     *                图片大小,单位为一个单元格的宽或高 => region[2]: width; region[3]: height
     * @param patriarch
     * @param workbook
     */
    public static void pictureToPosition(String imgPath, int[] region, HSSFPatriarch patriarch, Workbook workbook) {
        try {
            if (region.length != 4){
                throw new IllegalArgumentException("the region should have 4 items which are col, row, width, height for image");
            }
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bufferImg = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferImg, FilenameUtils.getExtension(imgPath), byteArrayOut);
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) region[0], region[1], (short) (region[0]+region[2]), region[1]+region[3]);
            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
            byteArrayOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //为合并区域设置边框
    public static void setBorderForRegion(int border, CellRangeAddress region, HSSFSheet sheet, Workbook wb) {
        RegionUtil.setBorderBottom(border, region, sheet, wb);
        RegionUtil.setBorderLeft(border, region, sheet, wb);
        RegionUtil.setBorderRight(border, region, sheet, wb);
        RegionUtil.setBorderTop(border, region, sheet, wb);
    }
}
