package com.ragab.ahmed.educational.happenings.ui.submit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ragab.ahmed.educational.happenings.R;

import java.lang.reflect.Field;

/**
 * Created by Ragabov on 4/7/2016.
 */
public class SubmitTypeAdapter extends ArrayAdapter<String> {

    //used as basis for the drawables names
    String baseName;
    LayoutInflater mLayLayoutInflater;
    int mResource;

    public SubmitTypeAdapter(Context context, int resource, String[] objects, String baseName) {
        super(context, resource, objects);
        mLayLayoutInflater = LayoutInflater.from(context);
        this.baseName = baseName + "_";
        this.mResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = mLayLayoutInflater.inflate(mResource, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.spinner_item_icon);
            holder.text = (TextView) convertView.findViewById(R.id.spinner_item_txt);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)convertView.getTag();
        holder.text.setText(getItem(position));

        String name = baseName + position;

        final Field field;
        try {
            field = R.drawable.class.getField(name);
            int id = field.getInt(null);
            holder.icon.setImageResource(id);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder
    {
        ImageView icon;
        TextView text;
    }
}
