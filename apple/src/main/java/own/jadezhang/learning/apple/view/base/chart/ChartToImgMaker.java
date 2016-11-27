package own.jadezhang.learning.apple.view.base.chart;

import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/25.
 */
public interface ChartToImgMaker {

    //用于配置图表标题，作为trans()的第一个参数option的key
    String TITLE_KEY = "titleKey";

    //用于配置图表X轴名称，作为trans()的第一个参数option的key
    String X_AXIS_KEY = "xAxisKey";

    //用于配置图表Y轴名称，作为trans()的第一个参数option的key
    String Y_AXIS_KEY = "yAxisKey";

    String trans(Map<String, String> option,Map<String, Object> data ,String imgPath);

    String trans(Map<String, String> option, Map<String, Object> data, String imgPath, int width, int height);

}
