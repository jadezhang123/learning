package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/11/25.
 */
public class PieChartToImgMaker extends AbstractChartToImgMaker {

    protected PieSectionLabelGenerator pieSectionLabelGenerator;

    public PieChartToImgMaker(){
        super();
    }

    public PieChartToImgMaker(PieSectionLabelGenerator pieSectionLabelGenerator){
        this.pieSectionLabelGenerator = pieSectionLabelGenerator;
    }

    @Override
    protected Dataset createDataset(Map<String, Object> data) {
        DefaultPieDataset dpd = new DefaultPieDataset();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            dpd.setValue(entry.getKey(), (Integer) entry.getValue());
        }
        return dpd;
    }

    @Override
    protected JFreeChart createChart(Map<String, String> option, Dataset dataset) {
        DefaultPieDataset dpd = (DefaultPieDataset) dataset;
        JFreeChart chart = ChartFactory.createPieChart(option.get(ChartToImgMaker.TITLE_KEY), dpd, true, true, false);
        PiePlot pieplot = (PiePlot) chart.getPlot();
        pieplot.setLabelFont(new Font("宋体", 0, 12));
        pieplot.setNoDataMessage("无数据");
        pieplot.setCircular(true);
        pieplot.setLabelGap(0.02D);
        if (pieSectionLabelGenerator == null){
            pieSectionLabelGenerator = new StandardPieSectionLabelGenerator(
                    "{0}: {1} ({2})",
                    NumberFormat.getNumberInstance(),
                    new DecimalFormat("0.00%"));
        }
        pieplot.setLabelGenerator(pieSectionLabelGenerator);
        return chart;
    }

    public void setPieSectionLabelGenerator(PieSectionLabelGenerator pieSectionLabelGenerator) {
        this.pieSectionLabelGenerator = pieSectionLabelGenerator;
    }
}