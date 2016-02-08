
package com.mikephil.charting.data;

import android.graphics.Path;

import com.mikephil.charting.charts.ScatterChart.ScatterShape;
import com.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet {

    /**
     * the size the scattershape will have, in screen pixels
     */
    private float mShapeSize = 15f;

    /**
     * the type of shape that is set to be drawn where the values are at,
     * default ScatterShape.SQUARE
     */
    private ScatterShape mScatterShape = ScatterShape.SQUARE;

    /**
     * Custom path object the user can provide that is drawn where the values
     * are at. This is used when ScatterShape.CUSTOM is set for a DataSet.
     */
    //private Path mCustomScatterPath = null;
    public ScatterDataSet(List<Entry> yVals, String label) {
        super(yVals, label);

        // mShapeSize = Utils.convertDpToPixel(8f);
    }

    @Override
    public DataSet<Entry> copy() {

        List<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < mYVals.size(); i++) {
            yVals.add(mYVals.get(i).copy());
        }

        ScatterDataSet copied = new ScatterDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mShapeSize = mShapeSize;
        copied.mScatterShape = mScatterShape;
        //copied.mCustomScatterPath = mCustomScatterPath;
        copied.mHighLightColor = mHighLightColor;

        return copied;
    }

    /**
     * Sets the size in density pixels the drawn scattershape will have. This
     * only applies for non custom shapes.
     *
     * @param size
     */
    public void setScatterShapeSize(float size) {
        mShapeSize = Utils.convertDpToPixel(size);
    }

    @Override
    public float getScatterShapeSize() {
        return mShapeSize;
    }

    /**
     * Sets the shape that is drawn on the position where the values are at.
     *
     * @param shape
     */
    public void setScatterShape(ScatterShape shape) {
        mScatterShape = shape;
    }

    @Override
    public ScatterShape getScatterShape() {
        return mScatterShape;
    }

//    /**
//     * Sets a path object as the shape to be drawn where the values are at. Do
//     * not forget to call setScatterShape(...) and set the shape to
//     * ScatterShape.CUSTOM.
//     *
//     * @param shape
//     */
//    public void setCustomScatterShape(Path shape) {
//        mCustomScatterPath = shape;
//    }
//
//    /**
//     * returns the custom path / shape that is specified to be drawn where the
//     * values are at
//     *
//     * @return
//     */
//    public Path getCustomScatterShape() {
//        return mCustomScatterPath;
//    }
}