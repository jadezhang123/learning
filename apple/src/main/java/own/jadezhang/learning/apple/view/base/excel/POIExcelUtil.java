package own.jadezhang.learning.apple.view.base.excel;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import own.jadezhang.learning.apple.domain.base.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Zhang Junwei on 2016/11/22.
 */
public class POIExcelUtil {

    //格式化器
    private static DecimalFormat integerFormat = new DecimalFormat("0");// 格式化 number String
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");// 格式化数字

    /**
     * 为表格的特定区域绑定下拉框约束
     * @param list   下拉框约束内容
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
     * @param region    图片位置以及大小；
     *                  图片左上角所在单元格 => region[0]：col; region[1]: row;
     *                  图片大小,单位为一个单元格的宽或高 => region[2]: width; region[3]: height
     * @param patriarch
     * @param workbook
     */
    public static void pictureToPosition(String imgPath, int[] region, HSSFPatriarch patriarch, Workbook workbook) {
        try {
            if (region.length != 4) {
                throw new IllegalArgumentException("the region should have 4 items which are col, row, width, height for image");
            }
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bufferImg = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferImg, FilenameUtils.getExtension(imgPath), byteArrayOut);
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) region[0], region[1], (short) (region[0] + region[2]), region[1] + region[3]);
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


    /**
     * 将excel行转为List，要求实体的各属性使用@ExcelColProAnnotation注解确定excel列名与实体属性对应关系
     * @param clazz 实体类的class实例
     * @param is    excel文件流
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(Class<T> clazz, InputStream is) throws Exception {
        List<T> resultList = new ArrayList<T>();
        Workbook workbook = WorkbookFactory.create(is);
        //默认读取第一页表格，第一行为列名行，其余为数据行
        Sheet sheet = workbook.getSheetAt(0);
        //列名-字段名
        Map<String, String> columnMap = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColProAnnotation annotation = field.getAnnotation(ExcelColProAnnotation.class);
            if (annotation != null) {
                if (!columnMap.containsKey(annotation.columnName())) {
                    columnMap.put(annotation.columnName(), field.getName());
                }
            }
        }
        //临时变量
        T t = null;
        Object value = null;
        Row row = null;
        Cell cell = null;
        for (int i = 1, maxRow = sheet.getLastRowNum(); i <= maxRow; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            t = clazz.newInstance();
            //列，
            for (int j = 0, maxCol = row.getLastCellNum(); j <= maxCol; j++) {
                cell = row.getCell(j);
                value = getCellValue(cell);
                if (value == null || "".equals(value)) {
                    continue;
                }
                //获取列名
                String columnName = sheet.getRow(0).getCell(j).toString();
                BeanUtils.setProperty(t, columnMap.get(columnName), value);
            }
            resultList.add(t);
        }
        return resultList;
    }

    public static <T> void toExcel(List<T> data, String sheetName, OutputStream os) throws IOException {
        if (data == null || data.size() == 0) {
            return;
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        Field[] fields = data.get(0).getClass().getDeclaredFields();
        List<ExcelColProAnnotation> colProAnnotations = new ArrayList<ExcelColProAnnotation>();
        ExcelColProAnnotation annotation = null;
        for (Field field : fields) {
            annotation = field.getAnnotation(ExcelColProAnnotation.class);
            if (annotation != null) {
                colProAnnotations.add(annotation);
            }
        }
        Collections.sort(colProAnnotations, new Comparator<ExcelColProAnnotation>() {
            @Override
            public int compare(ExcelColProAnnotation o1, ExcelColProAnnotation o2) {
                return o1.order() - o2.order();
            }
        });

        int colCount = colProAnnotations.size();
        int rowCount = data.size() + 1;

        //创建头部
        HSSFCellStyle headerStyle = POIExcelStyleFactory.headerStyle(workbook, IndexedColors.GREY_40_PERCENT.getIndex());
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(POIExcelStyleFactory.ROW_HEIGHT_iN_POINTS);
        for (int i = 0; i < colCount; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(colProAnnotations.get(i).columnName());
        }
        HSSFCellStyle cellStyle = POIExcelStyleFactory.defaultStyle(workbook);
        for (int i = 0; i < rowCount; i++) {
            HSSFRow row = sheet.createRow(i);
            row.setHeightInPoints(POIExcelStyleFactory.ROW_HEIGHT_iN_POINTS);
            for (int j = 0; j < colCount; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        workbook.write(os);
        os.flush();
    }

    public static Object getCellValue(Cell cell) {
        Object value = null;
        if (cell == null) {
            return value;
        }
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    value = integerFormat.format(cell.getNumericCellValue());
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = decimalFormat.format(cell.getNumericCellValue());
                } else {
                    value = timeFormat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                value = cell.toString();
        }
        return value;
    }

    public static void main(String[] args) throws Exception {
        List<User> users = POIExcelUtil.readExcel(User.class, new FileInputStream("G:\\tmp\\users.xlsx"));
        for (User user : users) {
            System.out.println(user);
        }
    }
}
