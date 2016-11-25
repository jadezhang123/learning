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
public class ExcelUtil {
    /**
     * 为表格的特定区域绑定下拉框约束
     *
     * @param list   下来框内容
     * @param region 作用区域，长度为4的int数组， region[0]: firstRow, region[1]: lastRow, region[2]: firstCol, region[3]: lastCol
     * @param sheet
     */
    public static void explicitListConstraint(String[] list, int[] region, HSSFSheet sheet) {

        if (region.length != 4) {
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), "下拉框区域数据必须设置完全");
        }

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


    public static void pictureToPosition(String imgPath, int[] region, HSSFPatriarch patriarch, Workbook workbook) {
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bufferImg = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferImg, FilenameUtils.getExtension(imgPath), byteArrayOut);
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 150, 1000, 210, (short) 0, 0, (short) 1, 1);
            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
            byteArrayOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pictureToCell(String imgPath, int[] cell, Drawing drawing, CreationHelper helper, Workbook workbook) {
        pictureToCell(imgPath, cell, null, drawing, helper, workbook);
    }

    /**
     * 将图片添加到指定的单元格
     * @param imgPath
     * @param cell   单元格位置 => cell[0] : row; cell[1]: col
     * @param size  图片大小 => size[0]: width; size[1]: height
     * @param drawing
     * @param helper
     * @param workbook
     */
    public static void pictureToCell(String imgPath, int[] cell, double[] size, Drawing drawing, CreationHelper helper, Workbook workbook) {
        try {
            InputStream is = new FileInputStream(imgPath);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setRow1(cell[0]);
            anchor.setCol1(cell[1]);
            Picture picture = drawing.createPicture(anchor, pictureIdx);
            if (size == null || size.length == 0) {
                picture.resize();
            } else if (size.length == 1){
                picture.resize(size[0]);
            }else if (size.length == 2){
                picture.resize(size[0], size[1]);
            }
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
