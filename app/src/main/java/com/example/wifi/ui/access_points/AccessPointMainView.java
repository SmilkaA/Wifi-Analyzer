package com.example.wifi.ui.access_points;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wifi.R;


public class AccessPointMainView extends LinearLayout {

    private ImageView groupIndicatorView;
    private TextView ssidView;
    private TextView levelView;
    private TextView channelView;
    private TextView primaryFrequencyView;
    private ImageView securityImageView;
    private TextView distanceView;


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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccessPointMainView);
        try {
            Drawable groupIndicatorImage = typedArray.getDrawable(R.styleable.AccessPointMainView_groupIndicatorView);
            String ssidText = typedArray.getString(R.styleable.AccessPointMainView_ssidView);
            String levelText = typedArray.getString(R.styleable.AccessPointMainView_levelView);
            String channelText = typedArray.getString(R.styleable.AccessPointMainView_channelView);
            String primaryFrequencyText = typedArray.getString(R.styleable.AccessPointMainView_primaryFrequencyView);
            Drawable securityImage = typedArray.getDrawable(R.styleable.AccessPointMainView_securityImageView);
            String distanceText = typedArray.getString(R.styleable.AccessPointMainView_distanceView);

            groupIndicatorView.setImageDrawable(groupIndicatorImage);
            ssidView.setText(ssidText);
            levelView.setText(levelText);
            channelView.setText(channelText);
            primaryFrequencyView.setText(primaryFrequencyText);
            securityImageView.setImageDrawable(securityImage);
            distanceView.setText(distanceText);

        } finally {
            typedArray.recycle();
        }

    }

}
