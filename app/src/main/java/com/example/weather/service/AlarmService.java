package com.example.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.WeatherActivity;
import com.example.weather.gson.Weather;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        upDateWeather();
        upDateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long nowTime = SystemClock.elapsedRealtime() + 120 * 1000;
        Intent intentAlarm = new Intent(this, AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intentAlarm, 0);
        //manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nowTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    //定时任务，定时更新天气数据，但没有在活动中刷新展示，所以每次重新打开APP的时候，从缓存中读到最新数据
    private void upDateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sp.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            //拿到weatherId，再去请求新数据
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId
                    + "&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather1 = Utility.handleWeatherResponse(responseText);
                    if (weather1 != null && "ok".equals(weather1.status)) {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(AlarmService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void upDateBingPic() {
        String imageUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(imageUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AlarmService.this).edit();
                editor.putString("bing_pic", bingPic).apply();
            }
        });
    }
}