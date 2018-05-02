package com.example.jonib.jpicslibrary;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jonib.jpicslibrary.adapter.RecyclerViewAdapter;
import com.example.jonib.jpicslibrary.model.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pagination {

    Context context;
    private PullToLoadView pullToLoadView;
    private RecyclerView myList;
    private RecyclerViewAdapter adapter;
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    private String URL = "http://pastebin.com/raw/wgkJgazE";
    private int nextPage;
    private List<JsonObject> list_items = new ArrayList<>();

    public Pagination(Context context, PullToLoadView pullToLoadView) {
        this.context = context;
        this.pullToLoadView = pullToLoadView;

        myList = pullToLoadView.getRecyclerView();
        myList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        receiveObjectAsList();

        adapter = new RecyclerViewAdapter(context, list_items);
        myList.setAdapter(adapter);

        initializePaginator();
    }

    public void initializePaginator(){
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                loadData(nextPage);
            }

            @Override
            public void onRefresh() {
                adapter.clear();
                hasLoadedAll = false;
                loadData(1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });
        pullToLoadView.initLoad();
    }

    public void loadData(final int page){
        isLoading = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < list_items.size(); i++){
                    adapter.add(list_items.get(i));
                }

                pullToLoadView.setComplete();
                isLoading = false;
                nextPage = page + 1;

            }
        }, 3000);
    }

    private void receiveObjectAsList() {

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                list_items = Arrays.asList(gson.fromJson(response, JsonObject[].class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


}
