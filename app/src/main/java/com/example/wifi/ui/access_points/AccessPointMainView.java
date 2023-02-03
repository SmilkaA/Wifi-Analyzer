package com.example.wifi.ui.access_points;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wifi.R;


public class AccessPointMainView extends LinearLayout {

    private ImageView groupIndicatorView;
    private TextView ssidView;
    private TextView levelView;
    private TextView channelView;
    private TextView primaryFrequencyView;
    private ImageView securityImageView;
    private TextView distanceView;
    private TextView speedView;
    private TextView IPView;

    public AccessPointMainView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.access_point_main_view, this, true);

        groupIndicatorView = findViewById(R.id.groupIndicator);
        ssidView = findViewById(R.id.ssid);
        levelView = findViewById(R.id.level);
        channelView = findViewById(R.id.channel);
        primaryFrequencyView = findViewById(R.id.primaryFrequency);
        securityImageView = findViewById(R.id.securityImage);
        distanceView = findViewById(R.id.distance);
        speedView = findViewById(R.id.speed);
        IPView = findViewById(R.id.ip_address);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccessPointMainView);
        try {
            Drawable groupIndicatorImage = typedArray.getDrawable(R.styleable.AccessPointMainView_groupIndicatorView);
            String ssidText = typedArray.getString(R.styleable.AccessPointMainView_ssidView);
            String levelText = typedArray.getString(R.styleable.AccessPointMainView_levelView);
            String channelText = typedArray.getString(R.styleable.AccessPointMainView_channelView);
            String primaryFrequencyText = typedArray.getString(R.styleable.AccessPointMainView_primaryFrequencyView);
            Drawable securityImage = typedArray.getDrawable(R.styleable.AccessPointMainView_securityImageView);
            String distanceText = typedArray.getString(R.styleable.AccessPointMainView_distanceView);
            String speedText = typedArray.getString(R.styleable.AccessPointMainView_speedView);
            String IPText = typedArray.getString(R.styleable.AccessPointMainView_IPView);

            groupIndicatorView.setImageDrawable(groupIndicatorImage);
            ssidView.setText(ssidText);
            levelView.setText(levelText);
            channelView.setText(channelText);
            primaryFrequencyView.setText(primaryFrequencyText);
            securityImageView.setImageDrawable(securityImage);
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
        if (Integer.parseInt(level) > -35) {
            this.levelView.setTextColor(Color.GREEN);
        } else if (Integer.parseInt(level) > -55) {
            this.levelView.setTextColor(Color.YELLOW);
        } else if (Integer.parseInt(level) > -80) {
            this.levelView.setTextColor(Color.YELLOW);
        } else if (Integer.parseInt(level) > -90) {
            this.levelView.setTextColor(Color.RED);
        } else {
            this.levelView.setTextColor(Color.RED);
        }
    }

    public void setChannelView(String channelView) {
        this.channelView.setText(channelView);
    }

    public void setPrimaryFrequencyView(String primaryFrequencyView) {
        this.primaryFrequencyView.setText(primaryFrequencyView);
    }

    public void setSecurityImageView(ImageView securityImageView) {
        this.securityImageView = securityImageView;
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
