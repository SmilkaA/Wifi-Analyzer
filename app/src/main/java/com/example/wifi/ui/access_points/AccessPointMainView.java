package com.example.wifi.ui.access_points;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wifi.R;
import com.example.wifi.Utils;

public class AccessPointMainView extends LinearLayout {

    private final TextView ssidView;
    private final TextView levelView;
    private final TextView channelView;
    private final TextView primaryFrequencyView;
    private final ImageView levelImageInMain;
    private final TextView distanceView;
    private final TextView speedView;
    private final TextView IPView;

    public AccessPointMainView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.access_point_main_view, this, true);

        ssidView = findViewById(R.id.ssid);
        levelView = findViewById(R.id.level);
        channelView = findViewById(R.id.channel);
        primaryFrequencyView = findViewById(R.id.primaryFrequency);
        levelImageInMain = findViewById(R.id.levelImage_in_main);
        distanceView = findViewById(R.id.distance);
        speedView = findViewById(R.id.speed);
        IPView = findViewById(R.id.ip_address);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccessPointMainView);
        try {
            String ssidText = typedArray.getString(R.styleable.AccessPointMainView_ssidView);
            String levelText = typedArray.getString(R.styleable.AccessPointMainView_levelView);
            String channelText = typedArray.getString(R.styleable.AccessPointMainView_channelView);
            String primaryFrequencyText = typedArray.getString(R.styleable.AccessPointMainView_primaryFrequencyView);
            Drawable levelImage = typedArray.getDrawable(R.styleable.AccessPointMainView_levelImageView);
            String distanceText = typedArray.getString(R.styleable.AccessPointMainView_distanceView);
            String speedText = typedArray.getString(R.styleable.AccessPointMainView_speedView);
            String IPText = typedArray.getString(R.styleable.AccessPointMainView_IPView);

            ssidView.setText(ssidText);
            levelView.setText(levelText);
            channelView.setText(channelText);
            primaryFrequencyView.setText(primaryFrequencyText);
            levelImageInMain.setImageDrawable(levelImage);
            distanceView.setText(distanceText);
            speedView.setText(speedText);
            IPView.setText(IPText);

        } finally {
            typedArray.recycle();
        }
    }

    public void setSsidView(String ssidView) {
        this.ssidView.setText(ssidView);
    }

    public void setLevelView(String level) {
        this.levelView.setText(getResources().getString(R.string.level_value, level));
        Utils.setLevelTextColor(Integer.parseInt(level),this.levelView);
    }

    public void setChannelView(String channelView) {
        this.channelView.setText(channelView);
    }

    public void setPrimaryFrequencyView(String primaryFrequencyView) {
        this.primaryFrequencyView.setText(primaryFrequencyView);
    }

    public void setLevelImageView(Drawable levelImage) {
        this.levelImageInMain.setImageDrawable(levelImage);
    }

    public ImageView getLevelImageInMain() {
        return levelImageInMain;
    }

    public void setDistanceView(String distanceView) {
        this.distanceView.setText(distanceView);
    }

    public void setSpeedView(String speedView) {
        this.speedView.setText(speedView);
    }

    public void setIPView(String IPView) {
        this.IPView.setText(IPView);
    }
}
