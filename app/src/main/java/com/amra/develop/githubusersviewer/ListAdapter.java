package com.amra.develop.githubusersviewer;

/**
 * Created by Anton Stukov on 14.02.2015.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    Resources res;
    public ImageLoader imageLoader;
    ArrayList<User> userDataList;

    public ListAdapter(Context context, ArrayList<User> _userDataList) {
        ctx = context;
        userDataList = _userDataList;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(ctx.getApplicationContext());

    }

    // Elements number
    @Override
    public int getCount() {
        return userDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Element
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            view = lInflater.inflate(R.layout.list_item, parent, false);

            // to avoid much invokes of findViewById method
            ViewHolder holder = new ViewHolder();
            holder.hLogin =  (TextView) view.findViewById(R.id.login);
            holder.hAvatar = (ImageView) view.findViewById(R.id.avatar);
            view.setTag(holder);

        }

        if (userDataList.get(position) != null) {

            ViewHolder holder = (ViewHolder) view.getTag();
            holder.hLogin.setText(userDataList.get(position).login);

            // Lazy image loading
            imageLoader.DisplayImage(userDataList.get(position).avatar_url,  holder.hAvatar);
        }

        return view;

    }

    // Class to avoid much invokes of findViewById method
    static class ViewHolder {
        TextView hLogin;
        ImageView hAvatar;
    }



}