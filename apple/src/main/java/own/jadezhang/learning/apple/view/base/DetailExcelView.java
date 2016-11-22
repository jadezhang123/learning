package own.jadezhang.learning.apple.view.base;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.view.base.excel.ExcelStyleFactory;
import own.jadezhang.learning.apple.view.base.excel.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/28.
 */
public class DetailExcelView extends AbstractExcelView {

    //表头起始
    private static int HEADLINE_ROW = 3;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<User> users = (List<User>) model.get("users");
        String fileName = "测试表格.xls";
        setResponse(request, response, fileName);
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(fileName);
        sheet.setDefaultColumnWidth((short) 10);
        sheet.setDefaultRowHeightInPoints(ExcelStyleFactory.ROW_HEIGHT_iN_POINTS);
        String[] header = {"序号", "姓名", "性别"};

        int colCount = header.length;
        int rowCount = users.size() + HEADLINE_ROW;
        //创建说明部分
        CellRangeAddress commentRangeAddress = new CellRangeAddress(0, 0, 0, colCount - 1);
        sheet.addMergedRegion(commentRangeAddress);
        ExcelUtil.setBorderForRegion(CellStyle.BORDER_THIN, commentRangeAddress, sheet, workbook);
        HSSFCellStyle commentStyle = ExcelStyleFactory.commentStyle(workbook);
        HSSFRow commentRow = sheet.createRow(0);
        commentRow.setHeightInPoints(100);
        HSSFCell commentCell = commentRow.createCell(0);
        commentCell.setCellStyle(commentStyle);
        commentCell.setCellValue("导入模板说明：\r\n1、设置默认：选择是/否\r\n2、合同时间：格式为yyyy-mm-dd\r\n备注：若合同信息超过20条，请复制黑色线框并在里面填写（黑色线框外为非工作区）");

        //创建标题
        CellRangeAddress titleRangeAddress = new CellRangeAddress(1, 1, 0, colCount - 1);
        sheet.addMergedRegion(titleRangeAddress);
        ExcelUtil.setBorderForRegion(CellStyle.BORDER_THIN, titleRangeAddress, sheet, workbook);

        HSSFCellStyle titleStyleStyle = ExcelStyleFactory.titleStyle(workbook);
        HSSFRow titleRow = sheet.createRow(1);
        titleRow.setHeightInPoints(ExcelStyleFactory.TITLE_HEIGHT_iN_POINTS);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyleStyle);
        titleCell.setCellValue(fileName);

        //创建头部
        HSSFCellStyle headerStyle = ExcelStyleFactory.headerStyle(workbook);
        HSSFRow headerRow = sheet.createRow(2);
        headerRow.setHeightInPoints(ExcelStyleFactory.ROW_HEIGHT_iN_POINTS);
        for (int i = 0; i < colCount; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header[i]);
        }
        //锁定标题和头部
        sheet.createFreezePane(0, HEADLINE_ROW);

        HSSFCellStyle cellStyle = ExcelStyleFactory.defaultStyle(workbook);
        User user;
        int dataContentRow = users.size();
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        int[] commentRegion = {2, 2};
        for (int i = 0; i < dataContentRow; i++) {
            HSSFRow row = sheet.createRow(i + HEADLINE_ROW);
            row.setHeightInPoints(ExcelStyleFactory.ROW_HEIGHT_iN_POINTS);
            for (int j = 0; j < colCount; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                user = users.get(i);
                if (j == 0) {
                    cell.setCellValue(i + 1);
                }
                if (j == 1) {
                    cell.setCellValue(user.getName());
                    ExcelUtil.commentForCell("姓名\r\n15671569027", commentRegion, patriarch, cell);
                }
            }
        }
        String[] sexConstraint = {"男", "女"};
        ExcelUtil.explicitListConstraint(sexConstraint, new int[]{HEADLINE_ROW, rowCount - 1, 2, 2}, sheet);
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
        // 处理中文文件名
        response.setContentType("application/vnd.ms-excel");
        //兼容火狐
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        } else {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        }
    }
}
