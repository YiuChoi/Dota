package name.caiyao.dota;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


class GpsLocation {

    static void openBmap(Context context) {
        AMapLocationClient locationClient = new AMapLocationClient(context);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置定位模式
        option.setOnceLocation(true);
        locationClient.setLocationOption(option);
        locationClient.startLocation();
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (location != null) {
                    if (location.getErrorCode() == 0) {
                        final double lat_a = location.getLatitude();
                        final double lng_a = location.getLongitude();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://119.29.7.130:6007/friend/save").openConnection();
                                    httpURLConnection.setRequestMethod("POST");
                                    httpURLConnection.setDoOutput(true);
                                    String data = "lat=" + lat_a + "&lng=" + lng_a;
                                    OutputStream outputStream = httpURLConnection.getOutputStream();
                                    outputStream.write(data.getBytes());
                                    outputStream.flush();
                                    outputStream.close();

                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                    String response;
                                    while ((response = bufferedReader.readLine()) != null) {
                                        Log.i("TAG", data + " " + httpURLConnection.getResponseCode() + " " + response);
                                    }
                                    bufferedReader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    } else {
                        Log.i("TAG", "定位错误！" + location.getErrorCode());
                    }
                }
            }
        });
    }
}