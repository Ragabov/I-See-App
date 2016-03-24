package com.ragab.ahmed.educational.happenings.ui.around;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class AroundAdapter extends RecyclerView.Adapter<AroundAdapter.ViewHolder> {

    private Event[] mDataset;

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
    public AroundAdapter(Event[] myDataset) {
        mDataset = myDataset;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.userTextView.setText(mDataset[position].userName);
        holder.descriptionTextView.setText(mDataset[position].description);
        holder.nameTextView.setText(mDataset[position].name);
        holder.timeTextView.setText(mDataset[position].date);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}