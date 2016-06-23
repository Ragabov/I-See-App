package com.ragab.ahmed.educational.happenings.ui.drawer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ragab.ahmed.educational.happenings.R;

/**
 * Created by Ragabov on 3/9/2016.
 */
public class NavigationListViewAdapter extends BaseAdapter{

    LayoutInflater minflater;
    String[] titles;
    Drawable[] icons;
    int selectedColor, defaultColor, defaultIconColor;
    public NavigationListViewAdapter(Context context) {
        super();
        minflater = LayoutInflater.from(context);
        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.navigation_titles);
        icons = new Drawable[4];
        icons[0] = resources.getDrawable(R.drawable.ic_map_black_24dp);
        icons[1] = resources.getDrawable(R.drawable.ic_whatshot_black_24dp);
        icons[2] = resources.getDrawable(R.drawable.ic_edit_location_black_24dp);
        icons[3] = resources.getDrawable(R.drawable.ic_account_circle_black_24dp);
        selectedColor = resources.getColor(R.color.color_accent);
        defaultColor = resources.getColor(R.color.default_nav_bar);
        defaultIconColor = Color.parseColor("#87FFFFFF");
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =  minflater.inflate(R.layout.navigation_list_item, null);
        }

        ImageView imageview = ((ImageView)convertView.findViewById(R.id.navigation_list_action_icon));
        imageview.setImageDrawable(icons[position]);
        TextView textView = (TextView)convertView.findViewById(R.id.navigation_list_action_describtion);
        textView.setText(titles[position]);
        if (((ListView) parent).getCheckedItemPosition() == position)
        {
            imageview.setColorFilter(selectedColor);
            textView.setTextColor(selectedColor);
        }
        else
        {
            imageview.setColorFilter(defaultIconColor);
            textView.setTextColor(defaultColor);
        }


        return convertView;
    }
}
