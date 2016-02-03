package com.mikephil.charting.interfaces.dataprovider;

import com.mikephil.charting.components.YAxis.AxisDependency;
import com.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.mikephil.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
