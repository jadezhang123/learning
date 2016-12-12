package own.jadezhang.learning.apple.view.base.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public class POIExcelStyleFactory {

    public static int ROW_HEIGHT_iN_POINTS = 25;

    public static int TITLE_HEIGHT_iN_POINTS = 40;

    public static HSSFCellStyle commentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle commentStyle = workbook.createCellStyle();
        commentStyle.setAlignment(CellStyle.ALIGN_LEFT);
        commentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        commentStyle.setWrapText(true);

        HSSFFont font = font(workbook, (short) 10, Font.BOLDWEIGHT_NORMAL);
        commentStyle.setFont(font);

        return commentStyle;
    }

    public static HSSFCellStyle titleStyle(HSSFWorkbook workbook) {
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setWrapText(true);

        HSSFFont font = font(workbook, (short) 15, Font.BOLDWEIGHT_BOLD);
        titleStyle.setFont(font);

        return titleStyle;
    }

    public static HSSFCellStyle viceTitleStyle(HSSFWorkbook workbook) {
        HSSFCellStyle viceTitleStyle = workbook.createCellStyle();
        viceTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        viceTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        HSSFFont font = font(workbook, (short) 12, Font.BOLDWEIGHT_BOLD);
        viceTitleStyle.setFont(font);
        viceTitleStyle.setWrapText(true);

        return viceTitleStyle;
    }

    public static HSSFCellStyle headerStyle(HSSFWorkbook workbook, short colorIndex) {
        HSSFCellStyle headerStyle = defaultStyle(workbook);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(colorIndex);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        return headerStyle;
    }

    public static HSSFCellStyle dateCellStyle(HSSFWorkbook workbook, String dateFormatStr) {
        HSSFCellStyle style = defaultStyle(workbook);
        short dateFormat = workbook.createDataFormat().getFormat(dateFormatStr);
        style.setDataFormat(dateFormat);
        return style;
    }

    public static HSSFCellStyle defaultStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        return style;
    }

    public static HSSFFont font(HSSFWorkbook workbook, short fontSize, short fontWeight) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints(fontSize);
        font.setBoldweight(fontWeight);
        return font;
    }

}
