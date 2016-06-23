package com.ragab.ahmed.educational.happenings.ui.favourites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.FavouriteLocation;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ragabov on 3/15/2016.
 */
public class FavouritesFragment extends Fragment {

    static final int SECTION_NUMBER = 0;

    MainActivity mainActivity;
    ArrayAdapter<FavouriteLocation> favourites_array;
    ProgressDialog mDialog;
    IseeApi mApi;
    public FavouritesFragment()
    {
        mApi = ApiHelper.buildApi();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(SECTION_NUMBER);

        favourites_array = new ArrayAdapter<FavouriteLocation>(mainActivity, R.layout.favourite_list_item, R.id.favourite_location_text);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        //fillShared(); for debugging only
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ListView listview = (ListView)view.findViewById(R.id.favourites_list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lat = favourites_array.getItem(position).lattitude;
                double lng = favourites_array.getItem(position).longitude;

                mainActivity.openEventsMap(lat, lng);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mainActivity);
                alert.setMessage(mainActivity.getResources().getString(R.string.delete_location_dialog_message));
                alert.setPositiveButton(mainActivity.getResources().getString(R.string.delete_positive_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.setMessage(getString(R.string.deleting_favorites_progress));
                        mDialog.show();

                        mApi.deleteFavouriteLocation(favourites_array.getItem(position).id).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == HttpURLConnection.HTTP_OK)
                                {
                                    favourites_array.remove(favourites_array.getItem(position));
                                    Toast.makeText(getActivity(), getString(R.string.operation_success), Toast.LENGTH_LONG);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), getString(R.string.operation_failure), Toast.LENGTH_LONG);
                                }
                                mDialog.hide();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getActivity(), getString(R.string.operation_failure), Toast.LENGTH_LONG);
                                mDialog.hide();
                            }
                        });

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton(mainActivity.getResources().getString(R.string.delete_negative_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });

        listview.setAdapter(favourites_array);


        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.current_location_fixed);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.openEventsMap(-1, -1);
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

        if (mainActivity.mUser != null)
            loadFavorites();

        return view;
    }

    public void loadFavorites ()
    {
        mDialog.setMessage(getString(R.string.loading_favorites_progress));

        mApi.getFavouriteLocation(mainActivity.mUser.id).enqueue(new Callback<FavouriteLocation[]>() {
            @Override
            public void onResponse(Call<FavouriteLocation[]> call, Response<FavouriteLocation[]> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    favourites_array.clear();
                    favourites_array.addAll(response.body());
                } else {
                    Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                }
                mDialog.hide();
            }

            @Override
            public void onFailure(Call<FavouriteLocation[]> call, Throwable t) {
                Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                mDialog.hide();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            double lat = data.getDoubleExtra(AddFavouriteActivity.LATITUDE, 0);
            double lng = data.getDoubleExtra(AddFavouriteActivity.LONGITUDE, 0);
            String name = data.getStringExtra(AddFavouriteActivity.NAME);

            final FavouriteLocation newLocation = new FavouriteLocation(name, lat, lng);

            mDialog.setMessage(getString(R.string.adding_favorites_progress));
            mDialog.show();
            mApi.addFavouriteLocation(name, lat, lng, mainActivity.mUser.id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        newLocation.id = response.body();
                        favourites_array.add(newLocation);
                        Toast.makeText(mainActivity, getString(R.string.operation_success), Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                    }
                    mDialog.hide();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                    mDialog.hide();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
