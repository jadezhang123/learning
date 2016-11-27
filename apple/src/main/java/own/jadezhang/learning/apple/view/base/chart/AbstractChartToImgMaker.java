package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.*;
import org.jfree.data.general.Dataset;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/25.
 */
public abstract class AbstractChartToImgMaker implements ChartToImgMaker{

    protected abstract Dataset createDataset(Map<String, Object> data);

    protected abstract JFreeChart createChart(Map<String, String> nameOption, Dataset dataset);

    protected ChartTheme setStandardChartThem() {
        //创建主题样式
        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN,15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);
        return standardChartTheme;
    }

    final protected String transToImg(JFreeChart chart, String imgPath, int width, int height) {
        try {
            OutputStream os = new FileOutputStream(imgPath);
            ChartUtilities.writeChartAsJPEG(os, chart, width, height);
            os.close();
            return imgPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String trans(Map<String, String> nameOption, Map<String, Object> data, String imgPath) {
        return trans(nameOption, data, imgPath, 500, 500);
    }

    @Override
    public String trans(Map<String, String> nameOption, Map<String, Object> data, String imgPath, int width, int height) {
        Dataset dataset = createDataset(data);
        setStandardChartThem();
        JFreeChart jFreeChart = createChart(nameOption, dataset);
        return transToImg(jFreeChart, imgPath, width, height);
    }
}
