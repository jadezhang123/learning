package own.jadezhang.learning.apple.view.base.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import own.jadezhang.common.domain.common.ErrorCodeEnum;
import own.jadezhang.common.exception.BizException;

/**
 * Created by Zhang Junwei on 2016/11/22.
 */
public class ExcelUtil {
    /**
     * 为表格的特定区域绑定下拉框约束
     *
     * @param list    下来框内容
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

    //为合并区域设置边框
    public static void setBorderForRegion(int border, CellRangeAddress region, HSSFSheet sheet, Workbook wb) {
        RegionUtil.setBorderBottom(border, region, sheet, wb);
        RegionUtil.setBorderLeft(border, region, sheet, wb);
        RegionUtil.setBorderRight(border, region, sheet, wb);
        RegionUtil.setBorderTop(border, region, sheet, wb);
    }
}
