package com.amra.develop.githubusersviewer;


import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Anton Stukov on 14.02.2015.
 */
public class UsersDataAsyncLoading extends AsyncTask<String, Void, String> {


    UsersFragment fragment;
    public UsersDataAsyncLoading(UsersFragment _fragment) {
        fragment = _fragment;
    }

   /* @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }*/

    @Override
    protected String doInBackground(String... params) {
        ServiceHandler sh = new ServiceHandler();
        // First element is our url
        String url = params[0];
        // First element is our url
        return sh.makeServiceCall(url, ServiceHandler.GET);
    }

    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            fragment.onUsersDataLoaded(result);
        } else {
            // If load nothing - annotate user about it.
            fragment.onFailToLoadData();
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

    }

   /* @Override
    protected void onProgressUpdate(Void... values) {}*/
}