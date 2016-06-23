package com.ragab.ahmed.educational.happenings.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.network.LocationHelper;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;
import com.ragab.ahmed.educational.happenings.ui.around.AroundAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ragabov on 6/21/2016.
 */
public class ProfileFragment extends Fragment {

    private int OPEN_CAMERA_REQUEST = 200;
    private int FROM_ALBUM_REQUEST = 201;

    IseeApi mApi;
    ArrayAdapter<Event> mAdapter;
    MainActivity mainActivity;
    CircleImageView profileView;

    @Override
    public void onAttach(Activity activity) {
        mApi = ApiHelper.buildApi();
        mAdapter = new ArrayAdapter<Event>(getActivity(), R.layout.history_item, R.id.texthistory);
        mainActivity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ListView listView = (ListView) view.findViewById(R.id.history_list_view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_profile_picture);
        profileView = (CircleImageView) view.findViewById(R.id.user_profile_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });
        TextView nameText = (TextView) view.findViewById(R.id.user_profile_name);
        nameText.setText(mainActivity.mUser.fname + " " + mainActivity.mUser.lname);
        listView.setAdapter(mAdapter);
        loadHistory();
        if (mainActivity.mUser.picPath != null)
        {
            Picasso.with(getActivity())
                    .load(mainActivity.mUser.picPath)
                    .resize(profileView.getWidth(), profileView.getHeight())
                    .centerCrop()
                    .into(profileView);
        }
        return view;
    }

    void loadHistory()
    {
        mApi.getUserHistory(mainActivity.mUser.id).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mAdapter.addAll(response.body());
                } else {
                    Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
            }
        });
    }
    void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int titlesId = R.array.image_dialog_picker_items;

        builder.setTitle(R.string.profile_dialog_picker_title)
                .setItems(titlesId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, FROM_ALBUM_REQUEST);
                                break;

                            case 1:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, OPEN_CAMERA_REQUEST);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        if (requestCode == FROM_ALBUM_REQUEST || requestCode == OPEN_CAMERA_REQUEST)
        {
            if(resultCode == Activity.RESULT_OK){
                Uri selectedImage = imageReturnedIntent.getData();
                final File finalFile = selectedImage == null ? null : new File(getRealPathFromURI(selectedImage));
                MediaType mediaType = MediaType.parse("image/*");
                RequestBody requestBody = RequestBody.create(mediaType, finalFile);
                MultipartBody.Part part = MultipartBody.Part.createFormData("profilepic", finalFile.getName(), requestBody);

                mApi.updateProfilePicture(mainActivity.mUser.id, part).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK && profileView != null) {
                            String filePath = ApiHelper.BASE_IMAGE_URL + response.body();
                            Picasso.with(getActivity())
                                    .load(filePath)
                                    .resize(profileView.getWidth(), profileView.getHeight())
                                    .centerCrop()
                                    .into(profileView);
                            mainActivity.updateNavProfilePicture(filePath);
                            mainActivity.mUser.picPath = filePath;
                        } else {
                            Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(mainActivity, getString(R.string.operation_failure), Toast.LENGTH_LONG);
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mainActivity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}

