package com.ragab.ahmed.educational.happenings.ui.around;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

import java.net.HttpURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class AroundAdapter extends RecyclerView.Adapter<AroundAdapter.ViewHolder> {

    private Event[] mDataset;
    IseeApi mApi;
    private MainActivity mainActivity;
    ProgressDialog mDialog;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CircleImageView iconImageView;
        public TextView userTextView;
        public TextView timeTextView;
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView confirmBtn;
        public ImageView disconfirmBtn;
        public ImageView showBtn;

        public ViewHolder(View v) {
            super(v);

            iconImageView = (CircleImageView)v.findViewById(R.id.event_card_icon);
            userTextView = (TextView)v.findViewById(R.id.event_card_user_txt);
            timeTextView = (TextView)v.findViewById(R.id.event_card_time_txt);
            nameTextView = (TextView)v.findViewById(R.id.event_card_name_txt);
            descriptionTextView = (TextView)v.findViewById(R.id.event_card_description_txt);
            confirmBtn = (ImageView)v.findViewById(R.id.event_card_confirm_btn);
            disconfirmBtn = (ImageView)v.findViewById(R.id.event_card_disconfirm_btn);
            showBtn = (ImageView)v.findViewById(R.id.event_card_show_btn);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AroundAdapter(Event[] myDataset, Activity activity) {
        mDataset = myDataset;
        this.mainActivity = (MainActivity) activity;
        mApi = ApiHelper.buildApi();
        mDialog = new ProgressDialog(activity);
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AroundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.around_event_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.userTextView.setText(mDataset[position].userName);
        holder.descriptionTextView.setText(mDataset[position].description);
        holder.nameTextView.setText(mDataset[position].name);
        holder.timeTextView.setText(mDataset[position].date);

        holder.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.openEventSubmitMap(mDataset[position].latitude, mDataset[position].longitude, mDataset[position].name);
            }
        });
        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mainActivity);
                alert.setMessage(mainActivity.getResources().getString(R.string.confirm_event_dialog_message));
                alert.setPositiveButton(mainActivity.getResources().getString(R.string.delete_positive_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.setMessage(mainActivity.getString(R.string.confirm_event_progress));
                        mDialog.show();
                        mApi.confirmEvent(mainActivity.mUser.id, mDataset[position].id).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == HttpURLConnection.HTTP_OK)
                                {
                                    Toast.makeText(mainActivity, mainActivity.getString(R.string.confirm_event_success), Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(mainActivity, mainActivity.getString(R.string.confirm_event_failure), Toast.LENGTH_LONG).show();
                                }
                                mDialog.hide();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(mainActivity, mainActivity.getString(R.string.confirm_event_failure), Toast.LENGTH_LONG).show();
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
            }
        });

        holder.disconfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mainActivity);
                alert.setMessage(mainActivity.getResources().getString(R.string.disconfirm_event_dialog_message));
                alert.setPositiveButton(mainActivity.getResources().getString(R.string.delete_positive_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.setMessage(mainActivity.getString(R.string.disconfirm_event_progress));
                        mDialog.show();
                        mApi.disconfirmEvent(mainActivity.mUser.id, mDataset[position].id).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == HttpURLConnection.HTTP_OK)
                                {
                                    Toast.makeText(mainActivity, mainActivity.getString(R.string.disconfirm_event_success), Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(mainActivity, mainActivity.getString(R.string.disconfirm_event_failure), Toast.LENGTH_LONG).show();
                                }
                                mDialog.hide();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(mainActivity, mainActivity.getString(R.string.disconfirm_event_failure), Toast.LENGTH_LONG).show();
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
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}