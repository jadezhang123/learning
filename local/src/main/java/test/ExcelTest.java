package test;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.Random;

/**
 * Created by Zhang Junwei on 2016/12/20 0020.
 */
public class ExcelTest {
    private static final String EXCEL_FILE = "D:\\chart.xls";
    private static final String VBS_FILE = "D:\\addChart.vbs";
    private static final String SHEET_NAME= "学生体测情况";
    private static final String[] COL_NAME = {"A", "B", "C", "D", "E", "F", "H", "I", "J", "K"};
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    Random random = new Random();

    public void readExcel(InputStream in) {
        try {
            wb = new HSSFWorkbook(new POIFSFileSystem(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
    }

    public void setData() {
        int bmiRowIndex = 5;

        HSSFRow bmiTitleRow = sheet.createRow(4);
        bmiTitleRow.createCell(1).setCellValue("学生BMI情况");

        String[] bmiDegree = new String[]{"正常", "低体重", "超重", "肥胖"};
        for (int index = 0; index < bmiDegree.length; index++) {
            String degree = bmiDegree[index];
            HSSFRow row = sheet.createRow(bmiRowIndex++);
            HSSFCell name = row.createCell(0);
            name.setCellValue(degree);
            HSSFCell value = row.createCell(1);
            value.setCellValue(random.nextInt(30));
        }

        HSSFName bmiStartNamedCell = wb.getName("bmiStart");
        if (bmiStartNamedCell == null) {
            bmiStartNamedCell = wb.createName();
            bmiStartNamedCell.setNameName("bmiStart");
        }
        bmiStartNamedCell.setRefersToFormula("学生体测情况!$A$5");

        HSSFName bmiEndNamedCell = wb.getName("bmiEnd");
        if (bmiEndNamedCell == null) {
            bmiEndNamedCell = wb.createName();
            bmiEndNamedCell.setNameName("bmiEnd");
        }
        bmiEndNamedCell.setRefersToFormula("学生体测情况!$B$9");

        setItemsData(33);
    }

    private void setItemsData(int rowIndex) {

        HSSFRow gradeRow = sheet.createRow(rowIndex++);
        HSSFName itemStartNamedCell = wb.getName("itemStart");
        if (itemStartNamedCell == null) {
            itemStartNamedCell = wb.createName();
            itemStartNamedCell.setNameName("itemStart");
        }
        itemStartNamedCell.setRefersToFormula("学生体测情况!$A$" + rowIndex);

        String[] grades = new String[]{"初一上", "初一下", "初二上", "初二下"};
        int colIndex = 1;
        HSSFCell gradeCell;
        for (String grade : grades) {
            gradeCell = gradeRow.createCell(colIndex++);
            gradeCell.setCellValue(grade);
        }
        String[] items = {"BMI", "肺活量", "50米跑", "1000米"};
        HSSFRow itemRow;
        for (String item : items) {
            itemRow = sheet.createRow(rowIndex++);
            itemRow.createCell(0).setCellValue(item);
            for (int i = 1; i <= grades.length; i++) {
                itemRow.createCell(i).setCellValue(random.nextInt(100));
            }
        }

        HSSFName itemEndNamedCell = wb.getName("itemEnd");
        if (itemEndNamedCell == null) {
            itemEndNamedCell = wb.createName();
            itemEndNamedCell.setNameName("itemEnd");
        }
        itemEndNamedCell.setRefersToFormula("学生体测情况!$" + COL_NAME[grades.length] + "$" + rowIndex);

    }

    public void saveExcel(String filePath) {
        try {
            OutputStream out = new FileOutputStream(filePath);
            wb.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ExcelTest test = new ExcelTest();
        test.readExcel(new FileInputStream(EXCEL_FILE));
        test.setData();
        test.saveExcel(EXCEL_FILE);

        /*Process process = null;
        String[] cmd = new String[]{"wscript", VBS_FILE};
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            if (process != null) {
                process.destroy();
            }
        }*/
    }
}
