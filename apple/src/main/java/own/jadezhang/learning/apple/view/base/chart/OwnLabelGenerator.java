package own.jadezhang.learning.apple.view.base.chart;

import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;

import java.text.NumberFormat;

/**
 * Created by Zhang Junwei on 2016/11/28.
 */
public class OwnLabelGenerator extends StandardPieSectionLabelGenerator {

    private String markedKey = "";

    public OwnLabelGenerator(String markedKey, String labelFormat, NumberFormat numberFormat, NumberFormat percentFormat) {
        super(labelFormat, numberFormat, percentFormat);
        this.markedKey = markedKey;
    }

    @Override
    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        if (markedKey.equals(key)){
            return super.generateSectionLabel(dataset,key) +"\r\n(你在这)";
        }
        return super.generateSectionLabel(dataset,key);
    }
}
