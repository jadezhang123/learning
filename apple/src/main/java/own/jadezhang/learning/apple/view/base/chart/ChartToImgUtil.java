package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/25.
 */
public class ChartToImgUtil {
    public static String trans(ChartToImgMaker maker, Map<String, String> nameOption, Map<String, Object> data, String imgPath, int width, int height){
        Dataset dataset = maker.createDataset(data);
        maker.setStandardChartThem();
        JFreeChart jFreeChart = maker.createChart(nameOption, dataset);
        return maker.transToImg(jFreeChart, imgPath, width, height);
    }
}
