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

    String trans(Map<String, String> nameOption,Map<String, Object> data ,String imgPath);

    String trans(Map<String, String> nameOption, Map<String, Object> data, String imgPath, int width, int height);

}
