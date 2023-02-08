package com.example.wifi.ui.channels_graph;

import android.content.Context;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.util.AttributeSet;

import com.example.wifi.Utils;

import java.util.List;
import java.util.Map.Entry;


public class LevelDiagram6GHz extends LevelDiagram {

	public LevelDiagram6GHz(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	@Override
    public void updateDiagram(List<ScanResult> scanResults) {
    	wlans.clear();
    	for (ScanResult sr : scanResults) {
    		if (Utils.getFrequencyBand(sr) == Utils.FrequencyBand.SIX_GHZ) {
				handleWifiDiagramItem(sr);
    		}
    	}
    	invalidate();
    }
    
    @Override
    protected void updateMeasures() {
    	super.updateMeasures();
    	rowsMarginLeft = getWidth() * 0.03f;
    	rowsMarginRight = getWidth() * 0.03f;
    }
    
    @Override
    public float getXAxisPos(int frequency) {
		int start 		= Utils.START_6GHZ_BAND - 10;
		int end 		= Utils.END_6GHZ_BAND + 10;
    	float margin 	= rowsMarginLeft + rowsMarginRight;
    	int relFreq 	= frequency - start;
    	int bandWidth 	= end - start;
    	float mghzWidth = (innerRect.right - innerRect.left - margin) / bandWidth;
    	
    	return innerRect.left + rowsMarginLeft + mghzWidth * relFreq;
    }
    
    @Override
    protected void drawXAxisLabelsAndLines(Canvas canvas) {
    	String s = Utils.START_6GHZ_BAND + " - " + Utils.END_6GHZ_BAND + " MHz";
		canvas.drawText(s, innerRect.left + innerRect.width() / 2, getHeight(), xLabelsPaint);

        for (Entry<Integer, Integer> entry : Utils.CHANNELS_6GHZ_BAND.entrySet()) {
    		float posX = getXAxisPos(entry.getKey());
    		canvas.drawLine(posX, innerRect.bottom, posX, innerRect.top, linesPaint);
        }
    }
    
    @Override 
    protected void drawSSIDLabels(Canvas canvas) {
		for (WifiDiagramItem wdi : wlans) {
			drawSSIDLabel(canvas, wdi, wdi.SSID + " (CH " + Utils.getChannel(wdi.frequency) + ")");
		}
    }
 }
