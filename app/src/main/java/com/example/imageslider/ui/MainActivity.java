package com.example.imageslider.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.imageslider.R;
import com.example.imageslider.adapter.SliderAdapterExample;
import com.example.imageslider.model.SliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SliderView sliderView;
    ArrayList<SliderItem> sliderItems = new ArrayList<>();
    private SliderAdapterExample adapter;

    private static final String HI = "http://192.168.1.66/wengky/api/beritaAcara";
    private final String Lokasi = "http://192.168.1.66/wengky/upload/berita/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        adapter    = new SliderAdapterExample(MainActivity.this,sliderItems );
        sliderView = findViewById(R.id.imageSlider);

        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        sliderItems.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(HI,
                response -> {
                    progressDialog.hide();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Log.d(String.valueOf(obj), "loadData: ");
                            SliderItem item = new SliderItem();
                            String Title = obj.getString("title");
                            String Image = obj.getString("image");
                            item.setImageUrl(Lokasi+Image);
                            item.setDescription(Title);
                            sliderItems.add(item);
                            progressDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    }
                    adapter.notifyDataSetChanged();

                }, error -> {
            progressDialog.hide();
            VolleyLog.e("terjadi error", "Error: " + error.getMessage());
            Toast.makeText(this, "Error"+ error, Toast.LENGTH_SHORT).show();

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jArr);

    }
}