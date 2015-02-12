package com.amra.develop.githubusersviewer;

/**
 * Created by Amra on 18.12.2014.
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


public class ListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    String[] logins, avatar_urls;
    Resources res;
    public ImageLoader imageLoader;


    public ListAdapter(Context context, String[] _logins, String[] _avatar_urls) {
        ctx = context;
        logins = _logins;
        avatar_urls = _avatar_urls;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //res = ctx.getResources();
        imageLoader = new ImageLoader(ctx.getApplicationContext());

    }

    // кол-во элементов
    @Override
    public int getCount() {
        return logins.length;
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return position;
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("MY", ": adapter.getView " + position);
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
            // to avoid much invokes of findViewById method
            ViewHolder holder = new ViewHolder();
            holder.hLogin =  (TextView) view.findViewById(R.id.login);
            holder.hAvatar = (ImageView) view.findViewById(R.id.avatar);
            view.setTag(holder);

        }
        if (logins[position] != null) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.hLogin.setText(logins[position]);
            // holder.hAvatar.set...
            imageLoader.DisplayImage(avatar_urls[position],  holder.hAvatar);
        }



        //TextView title =  (TextView) view.findViewById(R.id.login);
        //title.setText(logins[position]);

        //ImageView img = (ImageView) view.findViewById(R.id.avatar);

        // If image[position] is in cash - take it
       /* if (false) {

        }
        // load via AsyncTask
        else {

        }*/



        return view;
    }

    // Class to avoid much invokes of findViewById method
    static class ViewHolder {
        TextView hLogin;
        ImageView hAvatar;
    }



}