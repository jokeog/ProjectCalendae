package com.mikephil.charting.interfaces.dataprovider;

import com.mikephil.charting.components.YAxis;
import com.mikephil.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
