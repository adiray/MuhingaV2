package com.example.dell.muhingav2;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitClient {

    @GET("real_estate")
    Call<ArrayList<HousesResponse>> getAllHouses();


    @GET("real_estate")
    Call<ArrayList<HousesResponse>> getFilteredHouses(@QueryMap (encoded = true) Map<String, String> userFilters);


    @GET("real_estate?sortBy=created%20desc&pageSize=6&offset={offSet}")
    Call<ArrayList<HousesResponse>> getPagedHouses(@Path("offSet") Integer pageoffset);


    @GET("real_estate")
    Call<ArrayList<HousesResponse>> getQueryHouses(@QueryMap (encoded = true) Map<String, String> filters);

    //https://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/real_estate?sortBy=created%20desc&pageSize=6&offset=

    //https://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/real_estate?pageSize=4&offset=0&sortBy=created%20desc
}


//https://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/real_estate?props=Location
//https://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/real_estate?pageSize=4&offset=4&where=Location%3D'Muhinga'%20AND%20Rent%20%3D%20true&sortBy=updated%20desc