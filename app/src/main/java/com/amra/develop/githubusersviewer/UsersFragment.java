package com.amra.develop.githubusersviewer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Anton Stukov on 14.02.2015.
 */
public class UsersFragment extends Fragment {

    private static String GITHUB_USERS_URL_TEMPLATE = "https://api.github.com/users?since=";
    // Last loaded user id. "since" is the term of gitHub: https://developer.github.com/v3/users/#get-all-users
    private int since = 0;

    // String to store sum of all loaded batch of data about users
    public String combinedJSON="";

    // ArrayList to store text data about users
    private ArrayList<User> UserDataList;

    ListView list;
    boolean loadingNow = false;
    Button tryAgainButton;

    ListAdapter listAdapter;
    ImageLoader imagePreLoader;

    MainActivity act;
    Context context;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        act = (MainActivity)_activity;
        context = _activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_users, container, false);

        // ListView and it footer with progressBar.
        list = (ListView) rootView.findViewById(R.id.list);
        View footer = inflater.inflate(R.layout.list_footer, null);
        list.addFooterView(footer);

        // Set empty adapter to list.
        UserDataList = new ArrayList<User>();
        listAdapter = new ListAdapter (context, UserDataList);
        list.setAdapter(listAdapter);

        // Button if data not loaded for some reason.
        tryAgainButton = (Button) rootView.findViewById(R.id.button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain();
            }
        });

        imagePreLoader = new ImageLoader(container.getContext());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // If the some part of text data is already loaded - inflate list...
        if (!"".equals(combinedJSON)) {
            addUsersToList(getJSONArray(combinedJSON));
        }
        // ...else load data
        else {
            // First - check data in sharedPreferences
            SharedPreferences sPref = act.getPreferences(act.MODE_PRIVATE);
            String data = sPref.getString("USERS_DATA", "no data");
            // If exist - use it
            if (!"no data".equals(data)) {
                combinedJSON = data;
                addUsersToList(getJSONArray(combinedJSON));
            }
            // If no exist - load from Internet
            else {
                loadingNow = true;
                tryToLoadUserData();
            }
        }

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {}

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // If user scroll down at end - need to add items
                if ( firstVisibleItem+visibleItemCount == totalItemCount && !loadingNow) {
                    loadingNow = true;
                    tryToLoadUserData();
                }
            }

        });

    }

    public void tryToLoadUserData() {
        new UsersDataAsyncLoading(this).execute(GITHUB_USERS_URL_TEMPLATE+since);
    }

    public void  onUsersDataLoaded (String data) {

        // Add data from JSONArray to UserList
        JSONArray sourceArray = getJSONArray(data);
        addUsersToList(sourceArray);

        // Combine exist JSON string and obtained JSON string
        JSONArray destinationArray = getJSONArray(combinedJSON);
        try {
            for (int i = 0; i < sourceArray.length(); i++) {
                destinationArray.put(sourceArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e("----------------------- JSONException, no JSON added to combineJSON. --------------------------", "");
            e.printStackTrace();
        }
        combinedJSON = destinationArray.toString();

        // Save combined string in shared pref
        saveToSharedPreferences(combinedJSON);

        // Ready to load next batch of data
        loadingNow = false;

    }

    // Method return an JSONArray from String, if possible.
    private JSONArray getJSONArray(String data) {
        //JSONArray arr;
        try {
            return new JSONArray(data);
        } catch (JSONException e) {
            Log.e("----------------------- JSONException, empty JSON returned. --------------------------", "");
            e.printStackTrace();
        }
       return new JSONArray();
    }

    // Add data to ListView adapter and notify it.
    public void addUsersToList(JSONArray _jsonArray) {

        try {
            for(int i=0; i<_jsonArray.length(); i++){
                JSONObject obj = _jsonArray.getJSONObject(i);
                UserDataList.add(new User(obj.getString("login"),  obj.getString("avatar_url")));
            }
            listAdapter.notifyDataSetChanged();
            //JSONObject lastUserInBatch = _jsonArray.getJSONObject(i);
            since = Integer.parseInt(_jsonArray.getJSONObject(_jsonArray.length()-1).getString("id"));

        } catch (JSONException e) {
            Log.e("----------------------- JSONException --------------------------", "");
            e.printStackTrace();
        }

    }

    // "Cache" users data
    public void saveToSharedPreferences(String data) {
        // "Cache" users data to device
        SharedPreferences sPref = act.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("USERS_DATA", data);
        ed.apply();
    }

    // If data not loaded - allow to user try again.
    public void onFailToLoadData() {
        act.findViewById(R.id.try_again_panel).setVisibility(View.VISIBLE);
    }

    // If user press tryAgainButton - hide it.
    public void tryAgain() {
        act.findViewById(R.id.try_again_panel).setVisibility(View.GONE);
        tryToLoadUserData();

    }

    // Clear cache to test and test again.
    public void clearCache() {
        imagePreLoader.clearCache();
        SharedPreferences sPref = act.getPreferences(act.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("USERS_DATA", null);
        ed.apply();
    }

}
