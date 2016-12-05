package com.paulyung.pyphoto.utils;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.bean.Location;
import com.paulyung.pyphoto.bean.PhotoMsg;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by yang on 2016/12/5.
 * paulyung@outlook.com
 * <p>
 * http://restapi.amap.com/v3/geocode/regeo?parameters
 */

public class NetUtil {
    private static String gaodeAPI = "http://restapi.amap.com/v3/geocode/";
    private static String gaodeKEY = "4f2c38a37a132f44a58c2ef5c7a67e3d";

    /***
     * 根据经纬度获取城市信息
     */
    public static void getCitys(List<PhotoMsg> photoList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(gaodeAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        List<PhotoMsg> list = BaseApplication.getInstance().getPhotoMsg().get("Camera");
        String[] requestMsg = getRequestArray(photoList);
        CityService service = retrofit.create(CityService.class);
        int index = 0;
        for (int i = 0; i < requestMsg.length; ++i) {
            Map<String, String> params = new HashMap<>();
            params.put("key", gaodeKEY);
            params.put("location", requestMsg[i]);
            params.put("batch", "true");
            Call<Location> call = service.getCity(params);
            try {
                Location location = call.execute().body();//同步请求
                for (int j = 0; j < location.getRegeocodes().size(); ++j) {
                    Location.Regeocodes regeocodes = location.getRegeocodes().get(j);
                    list.get(index + j).setLocation(regeocodes.getFormatted_address());//详细信息
                    list.get(index + j).setCity(regeocodes.getAddressComponent().getProvince() + ' ' +
                            regeocodes.getAddressComponent().getCity());//省 市
                }
                index += 20;
            } catch (IOException e) {
                e.printStackTrace();
                index += 20;
            }
        }
    }

    /**
     * 规则： 最多支持20个坐标点。多个点之间用"|"分割。经度在前，纬度在后，经纬度间以“，”分割，经纬度小数点后不得超过6位
     */
    private static String[] getRequestArray(List<PhotoMsg> photoList) {
        int par1 = photoList.size() / 20;
        int par2 = photoList.size() % 20;
        int requestSize = par1;
        if (par2 != 0)
            requestSize++;
        String[] requests = new String[requestSize];
        for (int j = 0; j < requests.length; ++j) {
            String tmp = "";
            for (int i = 0; i < 20 && photoList.size() > 0; ++i) {
                PhotoMsg msg = photoList.get(0);
                tmp += msg.getLon();
                tmp += ',';
                tmp += msg.getLat();
                tmp += '|';
                photoList.remove(0);
            }
            requests[j] = tmp.substring(0, tmp.length() - 2);
        }
        return requests;
    }

    interface CityService {
        @GET("regeo")
        Call<Location> getCity(@QueryMap Map<String, String> map);
    }
}
