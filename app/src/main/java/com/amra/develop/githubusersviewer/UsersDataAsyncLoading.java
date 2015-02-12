package com.amra.develop.githubusersviewer;


import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anton Stukov
 * 12.02.2015
 */
public class UsersDataAsyncLoading extends AsyncTask<String, Void, String> {

    //String jsonStr;
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
        Log.i("MY", " result: "+result);
        String[] logins, avatar_urls;

       if (result != null) {
            try {
                // Create an JSON array from result string: [ {...}, ..., {...}]
                JSONArray jsonarray = new JSONArray(result);
                // Create simple arrays with data
                logins = new String[jsonarray.length()];
                avatar_urls = new String[jsonarray.length()];
                for(int i=0; i<jsonarray.length(); i++){
                    JSONObject obj = jsonarray.getJSONObject(i);
                    logins[i] = obj.getString("login");
                    avatar_urls[i] = obj.getString("avatar_url");
                }
                // pass array to fragment
                fragment.logins = logins;
                fragment.avatar_urls = avatar_urls;
                // build UI
                fragment.buildList();

            } catch (JSONException e) {
                Log.e("----------------------- JSONException --------------------------", "");
                e.printStackTrace();
            }
        } else {
            // If load nothing - annotate user about it.
            fragment.failToLoadData();
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        //

    }

   /* @Override
    protected void onProgressUpdate(Void... values) {}*/
}