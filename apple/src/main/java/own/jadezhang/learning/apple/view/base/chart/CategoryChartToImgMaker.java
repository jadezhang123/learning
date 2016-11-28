package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/26.
 */
public class CategoryChartToImgMaker extends AbstractChartToImgMaker {

    //用于配置X轴的类目列表， 作为trans()的第一个参数option的key
    public static final String SERIES_KEY_LIST = "series_key_list";

    //用于配置类目型表格的具体类型， 作为trans()的第一个参数option的key
    public static final String CATEGORY_CHART_TYPE_KEY = "categoryChartType";

    /**
     * CATEGORY_CHART_TYPE_* 的常量值指定生成表格的创建方法名，
     * 作为trans()的第一个参数option的key为 CATEGORY_CHART_TYPE_KEY 的value值
     */
    public static final String CATEGORY_CHART_TYPE_LINE = "createLineChart";

    public static final String CATEGORY_CHART_TYPE_LINE_3D = "createLineChart3D";

    public static final String CATEGORY_CHART_TYPE_BAR = "createBarChart";

    public static final String CATEGORY_CHART_TYPE_BAR_3D = "createBarChart3D";

    public CategoryChartToImgMaker() {
        super();
    }

    public CategoryChartToImgMaker(CategoryItemLabelGenerator itemLabelGenerator) {
        this.itemLabelGenerator = itemLabelGenerator;
    }

    /**
     * 组装数据, data数据格式要求：
     * 1. 必须添加key为LineChartToImgMaker.SERIES_KEY_LIST的value;value的类型必须为List<String>
     * 此项数据作为X轴类目数据
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
        /*一种针对更为简单的数据格式的组装数据实现，由于需要多层循环放弃使用
        List<Map<String, Double>> seriesData;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            seriesData = (List<Map<String, Double>>) entry.getValue();
            for (Map<String, Double> value : seriesData) {
                for (Map.Entry<String, Double> valueEntry : value.entrySet()) {
                    lineDataset.addValue(valueEntry.getValue(), entry.getKey(), valueEntry.getKey());
                }
            }
        }*/
        List<Number> seriesData;
        Number value;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (SERIES_KEY_LIST.equals(entry.getKey())) {
                continue;
            }
            seriesData = (List<Number>) entry.getValue();
            for (int index = 0, length = seriesData.size(); index < length; index++) {
                value = seriesData.get(index);
                lineDataset.addValue(value == null ? 0 : value, entry.getKey(), xAxisData.get(index));
            }
        }
        return lineDataset;
    }


    @Override
    protected JFreeChart createChart(Map<String, String> option, Dataset dataset) {
        DefaultCategoryDataset categoryDataset = (DefaultCategoryDataset) dataset;

        //获取配置信息中要创建的表格类型，从而决定调用的创建方法
        String createMethodName = option.get(CATEGORY_CHART_TYPE_KEY);

        JFreeChart chart;
        try {
            //通过方法名获取创建图表的方法
            Method method = ChartFactory.class.getMethod(createMethodName, new Class[]{
                    String.class, String.class, String.class,
                    CategoryDataset.class, PlotOrientation.class,
                    boolean.class, boolean.class, boolean.class}
            );
            chart = (JFreeChart) method.invoke(null, option.get(TITLE_KEY), option.get(X_AXIS_KEY), option.get(Y_AXIS_KEY),
                    categoryDataset, PlotOrientation.VERTICAL, true, false, false);
        } catch (Exception e) {
            //没有找到合适的创建方法，则默认创建折线图表
            chart = ChartFactory.createLineChart(option.get(TITLE_KEY), option.get(X_AXIS_KEY), option.get(Y_AXIS_KEY),
                    categoryDataset, PlotOrientation.VERTICAL, true, false, false);
        }

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
        plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setNoDataMessage("无数据");
        renderChartAsDefault(plot);
        return chart;

    }

    private void renderChartAsDefault(CategoryPlot plot) {
        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        if (itemLabelGenerator == null) {
            itemLabelGenerator = new StandardCategoryItemLabelGenerator();
        }
        categoryItemRenderer.setBaseItemLabelGenerator(itemLabelGenerator);
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        categoryItemRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        categoryItemRenderer.setBaseItemLabelFont(new Font("Dialog", 1, 14));
        plot.setRenderer(categoryItemRenderer);
    }

}
