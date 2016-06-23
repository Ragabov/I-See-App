package com.ragab.ahmed.educational.happenings.network;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ragabov on 3/28/2016.
 */
public class ApiHelper {

    private ApiHelper (){}

    public static final String SERVER_IP = "http://192.168.9.100/";


    public static final String BASE_URL = SERVER_IP + "index.php/IseeAPI/"; //172.31.249.32
    public static final String BASE_IMAGE_URL = SERVER_IP + "application/savefiles/";


    public static final String USER_ENDPOINT = "User";
    public static final String EVENT_ENDPOINT = "Event";

    public static IseeApi buildApi()
    {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IseeApi.class);
    }

    public static IseeApi buildApi(Converter.Factory factory)
    {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiHelper.BASE_URL)
                .addConverterFactory(factory)
                .build();

        return retrofit.create(IseeApi.class);
    }

}
