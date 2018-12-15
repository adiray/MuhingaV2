package com.example.dell.muhingav2;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Houses extends AppCompatActivity {

    //miscellaneous objects
    Boolean onRefreshing = false, infiniteLoading = false;  //shows weather the user is refreshing or loading more items respectively

    //declare view objects
    EditText housePriceEditText;
    CheckBox forRentCheck, forSaleCheck;
    Button filterHousesButton;
    SwipeRefreshLayout housesSwipeRefresh;  //swipe to refresh view for the houses recycler view

    //declare recycler view objects
    RecyclerView housesMainRecView;
    ArrayList<HousesResponse> allHousesResponseArray;   //holds all the houses objects that have been returned since the user last refreshed

    //declare the retrofit objects. All these are used with retrofit
    Retrofit.Builder builder;
    Retrofit myRetrofit;
    RetrofitClient myWebClient;
    retrofit2.Call<ArrayList<HousesResponse>> allHousesCall;
    Map<String, String> housesFilterMap = Collections.synchronizedMap(new HashMap<String, String>());  //holds the dynamic parameters used in the request url query
    Integer tableOffset = 0;   //this increases the offset from the top of the table when items are being retrieved from backendless
    String tableOffsetString = tableOffset.toString();

    //create our FastAdapter which will manage everything
    FastItemAdapter<HousesResponse> housesFastAdapter;
    FooterAdapter<ProgressItem> footerAdapter = new FooterAdapter<>();
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        //Initialize the views
        housesSwipeRefresh = findViewById(R.id.houses_swipe_refresh);

        //build out the main recycler view
        housesMainRecView = findViewById(R.id.houses_activity_rec_view);
        housesMainRecView.setHasFixedSize(true);
        housesMainRecView.setLayoutManager(new GridLayoutManager(Houses.this, 1, 1, false));

        //initialize our FastAdapter which will manage everything
        housesFastAdapter = new FastItemAdapter<>();

        //initialize the endless scroll listener
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(footerAdapter) {
            @Override
            public void onLoadMore(int currentPage) {

                footerAdapter.clear();
                footerAdapter.add(new ProgressItem().withEnabled(false));
                loadMoreHouses();
            }
        };


        //set the on refresh listener to the swipe to refresh view
        housesSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshHouses();
                Log.d("myLogsrefreshingvalue", onRefreshing.toString());
            }


        });


        //set the infinite/endless load on scroll listener to the recycler view
        housesMainRecView.addOnScrollListener(endlessRecyclerOnScrollListener);

        //fill the query map object for the retrofit query
        housesFilterMap.put("pageSize", "4");
        housesFilterMap.put("offset", tableOffsetString);
        housesFilterMap.put("sortBy", "created%20desc");


        requestHouses();    //make the initial / first  houses request


    }


    void requestHouses() {


        //initialize the retrofit client builder using the backendless.com api
        builder = new Retrofit.Builder();
        builder.baseUrl("http://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/")
                .addConverterFactory(GsonConverterFactory.create());

        //use your builder to build a retrofit object
        myRetrofit = builder.build();

        //create a retrofit client using the retrofit object
        myWebClient = myRetrofit.create(RetrofitClient.class);

        //create your call using the retrofit client
        allHousesCall = myWebClient.getQueryHouses(housesFilterMap);


        //make the call
        allHousesCall.clone().enqueue(new Callback<ArrayList<HousesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<HousesResponse>> call, Response<ArrayList<HousesResponse>> response) {

                if (!onRefreshing && !infiniteLoading) {

                    //perform the normal sequence of actions for a first time load
                    allHousesResponseArray = response.body();
                    housesFastAdapter.add(allHousesResponseArray);
                    housesMainRecView.setAdapter(footerAdapter.wrap(housesFastAdapter));


                    Log.d("myLogsRequestUrl", response.raw().request().url().toString());

                } else if (onRefreshing && !infiniteLoading) {

                    //perform the sequence of actions for a refreshed load
                    allHousesResponseArray.clear();
                    allHousesResponseArray = response.body();
                    housesFastAdapter.clear();
                    housesMainRecView.clearOnScrollListeners();
                    housesMainRecView.addOnScrollListener(endlessRecyclerOnScrollListener);
                    housesFastAdapter.add(response.body());
                    endlessRecyclerOnScrollListener.resetPageCount();


                    Log.d("myLogsRequestUrlOR", response.raw().request().url().toString());


                } else if (infiniteLoading && !onRefreshing) {

                    allHousesResponseArray.addAll(response.body());
                    footerAdapter.clear();
                    if (response.body().size() > 0) {
                        housesFastAdapter.add(response.body());
                    } else {
                        Toast.makeText(Houses.this, "No more items", Toast.LENGTH_LONG).show();
                    }


                    Log.d("myLogsRequestUrlIL", response.raw().request().url().toString() + " table offset = " + tableOffset);
                    infiniteLoading = false;


                }

                Log.d("myLogsOnSuccess", "onResponse: response successful");


            }

            @Override
            public void onFailure(Call<ArrayList<HousesResponse>> call, Throwable t) {

                Log.d("myLogsOnFailure", "onResponse: response unsuccessful");

            }
        });

    }

    //method called when user attempts to refresh the houses recycler view
    void refreshHouses() {

        tableOffset = 0;
        tableOffsetString = tableOffset.toString();
        housesFilterMap.put("offset", tableOffsetString);  //update the value of the offset in the request url
        onRefreshing = true;
        infiniteLoading = false;
        requestHouses();

        //stop the refreshing animation
        housesSwipeRefresh.setRefreshing(false);




        // housesMainRecView.addOnScrollListener(endlessRecyclerOnScrollListener);
        //onRefreshing = false;

    }

    void loadMoreHouses() {

        tableOffset = allHousesResponseArray.size();
        tableOffsetString = tableOffset.toString();
        housesFilterMap.put("offset", tableOffsetString);    //update the value of the offset in the request url
        Log.d("myLogs", "loadMoreHouses: " + housesFilterMap.toString());
        infiniteLoading = true;
        onRefreshing = false;
        requestHouses();
    }

}

/*  IMPORTANT INFORMATION
 * ISSUES
 * The infinite load will be triggered unexpectedly whenever the user refreshes the page because whenever the user refreshes
 * " endlessRecyclerOnScrollListener.resetPageCount(); "  is called which calls triggers the onLoadMore method
 *
 *
 *
 *
 *
 * */
