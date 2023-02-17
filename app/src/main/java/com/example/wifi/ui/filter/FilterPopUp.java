package com.example.wifi.ui.filter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterPopUp extends DialogFragment {

    private final List<ScanResult> data;
    private Context context;
    private EditText filterSSIDText;
    private TextView filterWifiBand2;
    private TextView filterWifiBand5;
    private TextView filterWifiBand6;
    private ImageView filterStrength0;
    private ImageView filterStrength1;
    private ImageView filterStrength2;
    private ImageView filterStrength3;
    private ImageView filterStrength4;
    private TextView filterSecurityNone;
    private TextView filterSecurityWPS;
    private TextView filterSecurityWEP;
    private TextView filterSecurityWPA;
    private TextView filterSecurityWPA2;
    private TextView filterSecurityWPA3;
    private Map<ImageView, Integer> colors;
    private final String PREFERENCES_PATH = "com.example.wifi.preferences";
    private MainActivity mainActivity;
    private OnCompleteListener mListener;

    public interface OnCompleteListener {
        void onComplete(List<ScanResult> resultList);
    }

    public FilterPopUp(Context context, List<ScanResult> data) {
        this.context = context;
        this.data = data;
        this.colors = new HashMap<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity) getActivity();
        this.mListener = (OnCompleteListener) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.filter_popup, null);
        initFields(dialogView);
        builder.setView(dialogView);
        builder.setMessage("FILTER").setIcon(R.drawable.ic_filter_list)
                .setPositiveButton("APPLY", (dialog, id) -> {
                    this.mListener.onComplete(applyFilters(data));
                    addValuesToPreferences(editor);
                    editor.apply();
                })
                .setNegativeButton("RESET", (dialogInterface, i) -> {
                    this.mListener.onComplete(mainActivity.getData());
                    editor.clear().apply();
                })
                .setNeutralButton("CLOSE", null);
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        String SSIDText = sharedPreferences.getString(getString(R.string.filter_ssid_title), "");
        filterSSIDText.setText(SSIDText);

        String wifiBand = sharedPreferences.getString(getString(R.string.filterWifiBand2), "");
        if (!wifiBand.equals("")) {
            filterWifiBand2.setTextColor(Integer.parseInt(wifiBand));
        }
        wifiBand = sharedPreferences.getString(getString(R.string.filterWifiBand5), "");
        if (!wifiBand.equals("")) {
            filterWifiBand5.setTextColor(Integer.parseInt(wifiBand));
        }
        wifiBand = sharedPreferences.getString(getString(R.string.filterWifiBand6), "");
        if (!wifiBand.equals("")) {
            filterWifiBand6.setTextColor(Integer.parseInt(wifiBand));
        }

        String imageS = sharedPreferences.getString(getString(R.string.filterStrength0), "");
        Bitmap imageB;
        if (!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            filterStrength0.setImageBitmap(imageB);
        }
        imageS = sharedPreferences.getString(getString(R.string.filterStrength1), "");
        if (!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            filterStrength1.setImageBitmap(imageB);
        }
        imageS = sharedPreferences.getString(getString(R.string.filterStrength2), "");
        if (!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            filterStrength2.setImageBitmap(imageB);
        }
        imageS = sharedPreferences.getString(getString(R.string.filterStrength3), "");
        if (!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            filterStrength3.setImageBitmap(imageB);
        }
        imageS = sharedPreferences.getString(getString(R.string.filterStrength4), "");
        if (!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            filterStrength4.setImageBitmap(imageB);
        }

        String wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityNone), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityNone.setTextColor(Integer.parseInt(wifiSecurity));
        }
        wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityWPS), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityWPS.setTextColor(Integer.parseInt(wifiSecurity));
        }
        wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityWEP), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityWEP.setTextColor(Integer.parseInt(wifiSecurity));
        }
        wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityWPA), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityWPA.setTextColor(Integer.parseInt(wifiSecurity));
        }
        wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityWPA2), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityWPA2.setTextColor(Integer.parseInt(wifiSecurity));
        }
        wifiSecurity = sharedPreferences.getString(getString(R.string.filterSecurityWPA3), "");
        if (!wifiSecurity.equals("")) {
            filterSecurityWPA3.setTextColor(Integer.parseInt(wifiSecurity));
        }
    }

    public void initFields(View view) {
        filterSSIDText = view.findViewById(R.id.filterSSIDText);
        filterWifiBand2 = view.findViewById(R.id.filterWifiBand2);
        filterWifiBand2.setOnClickListener(v ->
                setTextColorOnClick(filterWifiBand2, filterWifiBand2.getCurrentTextColor()));
        filterWifiBand5 = view.findViewById(R.id.filterWifiBand5);
        filterWifiBand5.setOnClickListener(view16 ->
                setTextColorOnClick(filterWifiBand5, filterWifiBand2.getCurrentTextColor()));
        filterWifiBand6 = view.findViewById(R.id.filterWifiBand6);
        filterWifiBand6.setOnClickListener(view16 ->
                setTextColorOnClick(filterWifiBand6, filterWifiBand2.getCurrentTextColor()));
        filterStrength0 = view.findViewById(R.id.filterStrength0);
        filterStrength0.setOnClickListener(view1 ->
                setDrawableColorOnClick(filterStrength0, Color.RED));
        filterStrength1 = view.findViewById(R.id.filterStrength1);
        filterStrength1.setOnClickListener(view12 ->
                setDrawableColorOnClick(filterStrength1, Color.RED));
        filterStrength2 = view.findViewById(R.id.filterStrength2);
        filterStrength2.setOnClickListener(view13 ->
                setDrawableColorOnClick(filterStrength2, Color.YELLOW));
        filterStrength3 = view.findViewById(R.id.filterStrength3);
        filterStrength3.setOnClickListener(view14 ->
                setDrawableColorOnClick(filterStrength3, Color.YELLOW));
        filterStrength4 = view.findViewById(R.id.filterStrength4);
        filterStrength4.setOnClickListener(view15 ->
                setDrawableColorOnClick(filterStrength4, Color.GREEN));
        filterSecurityNone = view.findViewById(R.id.filterSecurityNone);
        filterSecurityNone.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityNone, filterWifiBand2.getCurrentTextColor()));
        filterSecurityWPS = view.findViewById(R.id.filterSecurityWPS);
        filterSecurityWPS.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityWPS, filterWifiBand2.getCurrentTextColor()));
        filterSecurityWEP = view.findViewById(R.id.filterSecurityWEP);
        filterSecurityWEP.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityWEP, filterWifiBand2.getCurrentTextColor()));
        filterSecurityWPA = view.findViewById(R.id.filterSecurityWPA);
        filterSecurityWPA.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityWPA, filterWifiBand2.getCurrentTextColor()));
        filterSecurityWPA2 = view.findViewById(R.id.filterSecurityWPA2);
        filterSecurityWPA2.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityWPA2, filterWifiBand2.getCurrentTextColor()));
        filterSecurityWPA3 = view.findViewById(R.id.filterSecurityWPA3);
        filterSecurityWPA3.setOnClickListener(view16 ->
                setTextColorOnClick(filterSecurityWPA3, filterWifiBand2.getCurrentTextColor()));
        colors.put(filterStrength0, 0);
        colors.put(filterStrength1, 0);
        colors.put(filterStrength2, 0);
        colors.put(filterStrength3, 0);
        colors.put(filterStrength4, 0);
    }

    private void setTextColorOnClick(TextView textView, int color) {
        int[] textColors = new int[]{color, Color.GRAY};
        if (textView.getCurrentTextColor() == textColors[0]) {
            textView.setTextColor(textColors[1]);
        } else {
            textView.setTextColor(textColors[0]);
        }
    }

    private void setDrawableColorOnClick(ImageView imageView, int color) {
        if (!colors.containsKey(imageView)) {
            colors.put(imageView, 0);
            imageView.getDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (colors.get(imageView) % 2 == 0) {
                    colors.replace(imageView, 1);
                    imageView.getDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                } else {
                    colors.replace(imageView, 0);
                    imageView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    private void addValuesToPreferences(SharedPreferences.Editor editor) {
        editor.putString(getString(R.string.filter_ssid_title), filterSSIDText.getText().toString());
        editor.putString(getString(R.string.filterWifiBand2), String.valueOf(filterWifiBand2.getCurrentTextColor()));
        editor.putString(getString(R.string.filterWifiBand5), String.valueOf(filterWifiBand5.getCurrentTextColor()));
        editor.putString(getString(R.string.filterWifiBand6), String.valueOf(filterWifiBand6.getCurrentTextColor()));
        filterStrength0.buildDrawingCache();
        filterStrength1.buildDrawingCache();
        filterStrength2.buildDrawingCache();
        filterStrength3.buildDrawingCache();
        filterStrength4.buildDrawingCache();
        editor.putString(getString(R.string.filterStrength0), encodeImageToBase64(filterStrength0.getDrawingCache()));
        editor.putString(getString(R.string.filterStrength1), encodeImageToBase64(filterStrength1.getDrawingCache()));
        editor.putString(getString(R.string.filterStrength2), encodeImageToBase64(filterStrength2.getDrawingCache()));
        editor.putString(getString(R.string.filterStrength3), encodeImageToBase64(filterStrength3.getDrawingCache()));
        editor.putString(getString(R.string.filterStrength4), encodeImageToBase64(filterStrength4.getDrawingCache()));
        editor.putString(getString(R.string.filterSecurityNone), String.valueOf(filterSecurityNone.getCurrentTextColor()));
        editor.putString(getString(R.string.filterSecurityWPS), String.valueOf(filterSecurityWPS.getCurrentTextColor()));
        editor.putString(getString(R.string.filterSecurityWEP), String.valueOf(filterSecurityWEP.getCurrentTextColor()));
        editor.putString(getString(R.string.filterSecurityWPA), String.valueOf(filterSecurityWPA.getCurrentTextColor()));
        editor.putString(getString(R.string.filterSecurityWPA2), String.valueOf(filterSecurityWPA2.getCurrentTextColor()));
        editor.putString(getString(R.string.filterSecurityWPA3), String.valueOf(filterSecurityWPA3.getCurrentTextColor()));
    }

    private static String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public List<ScanResult> applyFilters(List<ScanResult> scanResults) {
        List<ScanResult> resultList = scanResults;

        resultList = checkSSID(resultList, String.valueOf(filterSSIDText.getText()));

        resultList = checkWifiBand(resultList, filterWifiBand2);
        resultList = checkWifiBand(resultList, filterWifiBand5);
        resultList = checkWifiBand(resultList, filterWifiBand6);

        resultList = checkSignalStrength(resultList);

        resultList = checkSecurity(resultList, filterSecurityNone);
        resultList = checkSecurity(resultList, filterSecurityWEP);
        resultList = checkSecurity(resultList, filterSecurityWPA);
        resultList = checkSecurity(resultList, filterSecurityWPA2);
        resultList = checkSecurity(resultList, filterSecurityWPA3);
        resultList = checkSecurity(resultList, filterSecurityWPS);

        return resultList;
    }

    public List<ScanResult> checkSSID(List<ScanResult> resultList, String SSID) {
        List<ScanResult> results = resultList;
        if (!SSID.equals("")) {
            results = new ArrayList<>();
            for (ScanResult scanResult : resultList) {
                if (scanResult.SSID.contains(SSID)) {
                    results.add(scanResult);
                }
            }
        }
        return results;
    }

    public List<ScanResult> checkWifiBand(List<ScanResult> resultList, TextView textView) {
        List<ScanResult> results = resultList;
        for (int i = 0; i < resultList.size(); i++) {
            Utils.FrequencyBand fBand = Utils.getFrequencyBand(resultList.get(i));
            if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ
                    && textView.getText().equals(getString(R.string.wifi_band_2ghz))
                    && textView.getCurrentTextColor() == Color.GRAY) {
                results.remove(resultList.get(i));
            } else if (fBand == Utils.FrequencyBand.FIVE_GHZ
                    && textView.getText().equals(getString(R.string.wifi_band_5ghz))
                    && textView.getCurrentTextColor() == Color.GRAY) {
                results.remove(resultList.get(i));
            } else if (fBand == Utils.FrequencyBand.SIX_GHZ
                    && textView.getText().equals(getString(R.string.wifi_band_6ghz))
                    && textView.getCurrentTextColor() == Color.GRAY) {
                results.remove(resultList.get(i));
            }
        }
        return results;
    }

    private List<ScanResult> checkSignalStrength(List<ScanResult> resultList) {
        List<ScanResult> results = resultList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i < resultList.size(); i++) {
                if (colors.get(filterStrength4) % 2 == 1 && resultList.get(i).level > -35) {
                    results.remove(resultList.get(i));
                } else if (colors.get(filterStrength3) % 2 == 1 && resultList.get(i).level > -55 && resultList.get(i).level <= -35) {
                    results.remove(resultList.get(i));
                } else if (colors.get(filterStrength2) % 2 == 1 && resultList.get(i).level > -80 && resultList.get(i).level <= -55) {
                    results.remove(resultList.get(i));
                } else if (colors.get(filterStrength1) % 2 == 1 && resultList.get(i).level > -90 && resultList.get(i).level <= -80) {
                    results.remove(resultList.get(i));
                } else if (colors.get(filterStrength0) % 2 == 1 && resultList.get(i).level <= -90) {
                    results.remove(resultList.get(i));
                }
            }
        }
        return results;
    }

    private List<ScanResult> checkSecurity(List<ScanResult> resultList, TextView filterSecurity) {
        List<ScanResult> results = resultList;
        if (filterSecurity.getCurrentTextColor() == Color.GRAY) {
            for (int i = 0; i < resultList.size(); i++) {
                if (filterSecurity.getText().equals(filterSecurityNone.getText())
                        && resultList.get(i).capabilities.isEmpty()) {
                    results.remove(resultList.get(i));
                } else if (resultList.get(i).capabilities.contains(filterSecurity.getText())) {
                    results.remove(resultList.get(i));
                }
            }
        }
        return results;
    }
}
