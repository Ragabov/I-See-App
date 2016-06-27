package com.ragab.ahmed.educational.happenings.ui.submit;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.data.models.User;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.network.LocationHelper;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;
import com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher.MyTextWatcher;
import com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher.Validator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends Fragment {


    private MainActivity mainActivity;

    private int OPEN_CAMERA_REQUEST = 200;
    private int FROM_ALBUM_REQUEST = 201;
    private int SECTION_NUMBER = 2;

    Uri selectedImage;

    Spinner categorySpinner, typeSpinner;
    EditText eventName, eventDescribtion;
    CheckBox isAnonymous;
    private ImageButton addImageButton;

    ProgressDialog mDialog ;
    IseeApi mApi;

    public SubmitFragment() {
        // Required empty public constructor
        mApi = ApiHelper.buildApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_submit, container, false);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage(getString(R.string.submit_event_progress));
        mDialog.setCancelable(false);

        eventName = (EditText) view.findViewById(R.id.event_name_txt);
        initValidators();

        eventDescribtion = (EditText) view.findViewById(R.id.event_description_txt);
        isAnonymous = (CheckBox) view.findViewById(R.id.anonymously_checkbox);

        addImageButton = (ImageButton)view.findViewById(R.id.add_event_image_btn);
        Drawable drawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_add_a_photo_black_36dp));
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.color_accent));
        addImageButton.setImageDrawable(drawable);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(selectedImage != null);
            }
        });


        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);

        String[] r = getResources().getStringArray(R.array.category_titles);
        categorySpinner.setAdapter(new SubmitTypeAdapter(getActivity(), R.layout.spinner_list_item, r, "cat", true));



        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    Field field = null;
                    int id;
                    try {
                        field = R.array.class.getField("types_" + pos);
                        id = field.getInt(null);
                        String[] r = getResources().getStringArray(id);
                        typeSpinner.setAdapter(new SubmitTypeAdapter(getActivity(), R.layout.spinner_list_item, r, "cat_"+ pos, false));
                        typeSpinner.setVisibility(View.VISIBLE);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                        return;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final LocationHelper locationHelper = new LocationHelper(getActivity(), -1, null);
        FloatingActionButton submitFab = (FloatingActionButton) view.findViewById(R.id.submit_event_fab);
        submitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eventName.getError() != null)
                    return ;

                mDialog.show();

                String name = eventName.getText().toString().trim();
                String description = eventDescribtion.getText().toString().trim();
                boolean anonymous = isAnonymous.isChecked();

                final Location location = locationHelper.getCurrentLocation();

                final File finalFile = selectedImage == null ? null : new File(getRealPathFromURI(selectedImage));
                MultipartBody.Part part = null;
                if (selectedImage != null) {
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    MediaType mediaType = MediaType.parse("image/*");
                    RequestBody requestBody = RequestBody.create(mediaType, finalFile);
                    part = MultipartBody.Part.createFormData("event_pic", finalFile.getName(), requestBody);
                }
                User user = mainActivity.mUser;
                int typeId = categorySpinner.getSelectedItemPosition() * 10 + typeSpinner.getSelectedItemPosition() + 1;

                mApi.submitEvent(name, description, location.getLongitude(), location.getLatitude(),
                        typeId, anonymous, user.id, part).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 201) {
                            Toast.makeText(getActivity(), getString(R.string.submit_event_success), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getString(R.string.submit_event_failure), Toast.LENGTH_LONG).show();
                        }
                        if (finalFile != null)
                         finalFile.delete();
                        mDialog.hide();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.submit_event_failure), Toast.LENGTH_LONG).show();
                        mDialog.hide();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(SECTION_NUMBER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        if (requestCode == FROM_ALBUM_REQUEST || requestCode == OPEN_CAMERA_REQUEST)
        {
            if(resultCode == Activity.RESULT_OK){
                selectedImage = imageReturnedIntent.getData();
                Bitmap sourceBitmap = null;
                try {
                    sourceBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    int height = sourceBitmap.getHeight() > 1024 ? 1024 :  sourceBitmap.getHeight();
                    int width = sourceBitmap.getWidth() > 1024 ? 1024 :  sourceBitmap.getWidth();
                    sourceBitmap = getResizedBitmap(sourceBitmap, width, height);
                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cur = mainActivity.getContentResolver().query(selectedImage, orientationColumn, null, null, null);
                    int orientation = -1;
                    if (cur != null && cur.moveToFirst()) {
                        orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    }
                    sourceBitmap = rotateImage(sourceBitmap, orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addImageButton.setImageBitmap(sourceBitmap);
                addImageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    void showImageDialog(boolean remove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int titlesId = (remove)? R.array.image_dialog_picker_items_extended : R.array.image_dialog_picker_items;

        builder.setTitle(R.string.image_dialog_picker_title)
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

                            case 2:
                                Drawable drawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_add_a_photo_black_36dp));
                                DrawableCompat.setTint(drawable, getResources().getColor(R.color.color_accent));
                                addImageButton.setImageDrawable(drawable);
                                addImageButton.setScaleType(ImageView.ScaleType.CENTER);
                                selectedImage = null;
                        }
                    }
                });
        builder.create().show();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mainActivity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void initValidators () {
        String namePrompt = getString(R.string.event_name_validation);

        eventName.addTextChangedListener(new MyTextWatcher(eventName, new Validator<Boolean, String>(namePrompt) {
            @Override
            public Boolean execute(String s) {
                return Event.isValidName(s);
            }
        }));
    }
}
