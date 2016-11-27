package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/26.
 */
public class LineChartToImgMaker extends AbstractChartToImgMaker {

    public static final String SERIES_KEY_LIST = "series_key_list";

    /**
     * 组装数据, data数据格式要求：
     * 1. 必须添加key为LineChartToImgMaker.SERIES_KEY_LIST的value;value的类型必须为List<String>
     * 此项数据作为X轴数据
     * 2. data 中其他key代表一个系列数据，value的数据类型必须为List<Number>
     * 某项系列的value 值对应的list的size必须和X轴数据的size相等，并且一一对应
     *
     * @param data
     * @return
     */
    @Override
    protected Dataset createDataset(Map<String, Object> data) {
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        List<String> xAxisData = (List<String>) data.get(SERIES_KEY_LIST);
        /*List<Map<String, Double>> seriesData;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            seriesData = (List<Map<String, Double>>) entry.getValue();
            for (Map<String, Double> value : seriesData) {
                for (Map.Entry<String, Double> valueEntry : value.entrySet()) {
                    lineDataset.addValue(valueEntry.getValue(), entry.getKey(), valueEntry.getKey());
                }
            }
        }*/
        List<Number> seriesData;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (SERIES_KEY_LIST.equals(entry.getKey())) {
                continue;
            }
            seriesData = (List<Number>) entry.getValue();
            for (int index = 0, length = seriesData.size(); index < length; index++) {
                lineDataset.addValue(seriesData.get(index), entry.getKey(), xAxisData.get(index));
            }
        }
        return lineDataset;
    }

    @Override
    protected JFreeChart createChart(Map<String, String> nameOption, Dataset dataset) {
        DefaultCategoryDataset lineDataset = (DefaultCategoryDataset) dataset;
        JFreeChart chart = ChartFactory.createLineChart(nameOption.get(TITLE_KEY), nameOption.get(X_AXIS_KEY), nameOption.get(Y_AXIS_KEY),
                lineDataset, PlotOrientation.VERTICAL, true, false, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
        plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
        plot.setRangeGridlinePaint(Color.GRAY);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setSeriesPaint(0, Color.black);    //设置折线的颜色
        renderer.setBaseShapesFilled(true);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelFont(new Font("Dialog", 1, 14));
        plot.setRenderer(renderer);
        return chart;
    }

}
