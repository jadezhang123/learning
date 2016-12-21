package own.jadezhang.learning.apple.view.base.chart;

import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.PieStyler;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Zhang Junwei on 2016/12/16 0016.
 */
public class ChartTest {
    public static void main(String[] args) throws IOException {
        double[] xData = new double[] { 0.0, 1.0, 2.0 };
        double[] yData = new double[] { 2.0, 1.0, 0.0 };
        XYChart chart = QuickChart.getChart("测试","X轴", "Y轴","y(x)",xData, yData);
        BitmapEncoder.saveBitmap(chart, "D:\\a.png", BitmapEncoder.BitmapFormat.PNG);

        trans(getPieChart(), "D:\\b.png");
    }

    public static void trans(Chart chart, String imgPath){
        try {
            BitmapEncoder.saveBitmap(chart, imgPath, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PieChart getPieChart(){
        // Create Chart
       /* PieChart chart = new PieChartBuilder().width(800).height(600).title("学生BMI情况").build();

        // Series
        chart.addSeries("正常", 100);
        chart.addSeries("低体重", 10);
        chart.addSeries("超重", 20);
        chart.addSeries("肥胖", 6);
        PieStyler styler = chart.getStyler();
        styler.setAnnotationType(PieStyler.AnnotationType.LabelAndPercentage);
        styler.setDrawAllAnnotations(true);

        return chart;*/

        PieChart chart = new PieChartBuilder().width(800).height(600).title("学生BMI情况").build();

        PieStyler styler = chart.getStyler();
        Color[] sliceColors = new Color[] {
                new Color(2, 127, 182), new Color(85, 245, 244),
                new Color(252, 145, 57), new Color(192, 192, 192)};
        styler.setSeriesColors(sliceColors);
        styler.setAnnotationType(PieStyler.AnnotationType.LabelAndPercentage);
        styler.setDrawAllAnnotations(true);

        // Series
        chart.addSeries("A", 0);
        chart.addSeries("B", 0);
        chart.addSeries("C", 4);
        chart.addSeries("D", 22);


        return chart;
    }
}
