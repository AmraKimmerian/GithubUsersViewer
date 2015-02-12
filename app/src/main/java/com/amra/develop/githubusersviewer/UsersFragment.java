package com.amra.develop.githubusersviewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Anton Stukov
 * 12.02.2015
 */
public class UsersFragment extends Fragment {

    private static String GITHUB_USERS_URL = "https://api.github.com/users";

    // Arrays to store text data about users
    public static String[] logins, avatar_urls;

    ListView list;
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

        View rootView = inflater.inflate(R.layout.list, container, false);

        list = (ListView) rootView.findViewById(R.id.list);
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

        // If the text data is already loaded - inflate list...
        if (logins != null) {
            buildList();
        }
        // ...else load data
        else {
            tryToLoadUserData();
        }
    }

    public void buildList() {
        act.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        listAdapter = new ListAdapter (context, logins, avatar_urls);
        list.setAdapter(listAdapter);
    }

    public void tryToLoadUserData() {
        new UsersDataAsyncLoading(this).execute(GITHUB_USERS_URL);
    }

    // If data not loaded - allow to user try again.
    public void failToLoadData() {
        act.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        act.findViewById(R.id.try_again_panel).setVisibility(View.VISIBLE);
    }

    // If user press tryAgainButton - hide it and show loader, while loading.
    public void tryAgain() {
        act.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        act.findViewById(R.id.try_again_panel).setVisibility(View.GONE);
        tryToLoadUserData();

    }
    // Clear cache to test and test again.
    public void clearCache() {
        imagePreLoader.clearCache();
    }
}
