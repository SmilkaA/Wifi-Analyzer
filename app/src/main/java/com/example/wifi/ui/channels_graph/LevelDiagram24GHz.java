package com.example.wifi.ui.channels_graph;


import android.content.Context;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.util.AttributeSet;

import com.example.wifi.Utils;

import java.util.List;
import java.util.Map;

public class LevelDiagram24GHz extends LevelDiagram {

    public LevelDiagram24GHz(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void updateDiagram(List<ScanResult> scanResults) {
        wlans.clear();
        for (ScanResult sr : scanResults) {
            if (Utils.getFrequencyBand(sr) == Utils.FrequencyBand.TWO_FOUR_GHZ) {
                handleWifiDiagramItem(sr);
            }
        }

        invalidate();
    }

    @Override
    protected void updateMeasures() {
        super.updateMeasures();
        rowsMarginLeft = getWidth() * 0.08f;
        rowsMarginRight = getWidth() * 0.03f;
    }

    @Override
    public float getXAxisPos(int frequency) {
        int start = Utils.START_24GHZ_BAND - 5;
        float margin = rowsMarginLeft + rowsMarginRight;
        int relFreq = frequency - start;
        int bandWidth = Utils.END_24GHZ_BAND - start;
        float mghzWidth = (innerRect.right - innerRect.left - margin) / bandWidth;

        return innerRect.left + rowsMarginLeft + mghzWidth * relFreq;
    }

    @Override
    protected void drawXAxisLabelsAndLines(Canvas canvas) {
        for (Map.Entry<Integer, Integer> entry : Utils.CHANNELS_24GHZ_BAND.entrySet()) {
            float posX = getXAxisPos(entry.getKey());
            canvas.drawLine(posX, innerRect.bottom, posX, innerRect.top, linesPaint);
            canvas.drawText(Integer.toString(entry.getValue()), posX, getHeight(), xLabelsPaint);
        }
    }

    @Override
    protected void drawSSIDLabels(Canvas canvas) {
        for (WifiDiagramItem wdi : wlans) {
            drawSSIDLabel(canvas, wdi, wdi.SSID);
        }
    }
}
