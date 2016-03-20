package com.ragab.ahmed.educational.happenings.ui.favourites;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

/**
 * Created by Ragabov on 3/15/2016.
 */
public class FavouritesFragment extends Fragment {

    public static final String FAVOURITES_FILE = "location_favorites";

    static final int SECTION_NUMBER = 0;

    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> favourites_array;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(SECTION_NUMBER);
        sharedPreferences = activity.getSharedPreferences(FAVOURITES_FILE, 0);

        //fillShared(); for debugging only

        favourites_array= new ArrayAdapter<String>(activity,R.layout.favourite_list_item,R.id.favourite_location_text);
        favourites_array.addAll(sharedPreferences.getAll().keySet());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ListView listview = (ListView)view.findViewById(R.id.favourites_list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] location =
                        sharedPreferences.getString(favourites_array.getItem(position), "0,0")
                        .split(",");

                double lat = Double.parseDouble(location[0]);
                double lng = Double.parseDouble(location[1]);

                mainActivity.openEventsMap(lat, lng);
            }
        });
        listview.setAdapter(favourites_array);


        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.current_location_fixed);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.openEventsMap(-1,-1);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.add_favourite_fab);
        floatingActionButton.show();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mainActivity.getApplicationContext(), AddFavouriteActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            double lat = data.getDoubleExtra(AddFavouriteActivity.LATITUDE, 0);
            double lng = data.getDoubleExtra(AddFavouriteActivity.LONGITUDE, 0);
            String location = String.valueOf(lat) + "," + String.valueOf(lng);
            String name = data.getStringExtra(AddFavouriteActivity.NAME);
            editor.putString(name, location);
            editor.commit();
            favourites_array.add(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void fillShared()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Home", "0,100");
        editor.putString("Some other place", "40.42,15.24");
        editor.putString("Yet another place", "14.30,123.50");
        editor.commit();
    }

}
