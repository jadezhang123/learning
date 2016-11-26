package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/25.
 */
public interface ChartToImgMaker {

    String TITLE_KEY = "titleKey";

    String X_AXIS_KEY = "xAxisKey";

    String Y_AXIS_KEY = "yAxisKey";

    Dataset createDataset(Map<String, Object> data);

    ChartTheme setStandardChartThem();

    JFreeChart createChart(Map<String, String> nameOption, Dataset dataset);

    String transToImg(JFreeChart chart, String imgPath, int width, int height);
}
