package com.mikephil.charting.interfaces.dataprovider;

import com.mikephil.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
