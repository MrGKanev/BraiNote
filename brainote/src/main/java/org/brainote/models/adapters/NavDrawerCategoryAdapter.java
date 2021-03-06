package org.brainote.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.brainote.BaseActivity;
import org.brainote.ListFragment;
import org.brainote.models.Category;
import org.brainote.utils.Constants;
import org.brainote.utils.Fonts;
import com.neopixl.pixlui.components.textview.TextView;

import org.brainote.R;

import java.util.ArrayList;


public class NavDrawerCategoryAdapter extends BaseAdapter {

    private Activity mActivity;
    private int layout;
    private ArrayList<Category> categories;
    private LayoutInflater inflater;


    public NavDrawerCategoryAdapter(Activity mActivity, ArrayList<Category> categories) {
        this(mActivity, categories, null);
    }


    public NavDrawerCategoryAdapter(Activity mActivity, ArrayList<Category> categories, String navigationTmp) {
        this.mActivity = mActivity;
        this.layout = R.layout.drawer_list_item;
        this.categories = categories;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categories.size();
    }


    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        // Finds elements
        Category category = categories.get(position);

        NoteDrawerCategoryAdapterViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, mActivity.getSharedPreferences(Constants.PREFS_NAME,
                    Context.MODE_MULTI_PROCESS), convertView);

            holder = new NoteDrawerCategoryAdapterViewHolder();

            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.count = (android.widget.TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (NoteDrawerCategoryAdapterViewHolder) convertView.getTag();
        }

        // Set the results into TextViews	
        holder.txtTitle.setText(category.getName());

        if (isSelected(parent, position)) {
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.txtTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.txtTitle.setTextColor(mActivity.getResources().getColor(R.color.drawer_text));
            holder.txtTitle.setTypeface(null, Typeface.NORMAL);
        }

        // Set the results into ImageView checking if an icon is present before
        if (category.getColor() != null && category.getColor().length() > 0) {
            Drawable img = mActivity.getResources().getDrawable(R.drawable.square);
            ColorFilter cf = new LightingColorFilter(Color.parseColor("#000000"), Integer.parseInt(category.getColor
                    ()));
            // Before API 16 the object is mutable yet
            if (Build.VERSION.SDK_INT >= 16) {
                img.mutate().setColorFilter(cf);
            } else {
                img.setColorFilter(cf);
            }
            holder.imgIcon.setImageDrawable(img);
            int padding = 12;
            holder.imgIcon.setPadding(padding, padding, padding, padding);
        }

        // Sets category count if set in preferences
        if (mActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS).getBoolean
                ("settings_show_category_count", false)) {
            holder.count.setText(String.valueOf(category.getCount()));
        }

        return convertView;
    }


    private boolean isSelected(ViewGroup parent, int position) {

        // Getting actual navigation selection
        String[] navigationListCodes = mActivity.getResources().getStringArray(
                R.array.navigation_list_codes);

        // Managing temporary navigation indicator when coming from a widget
        String navigationTmp = ListFragment.class.isAssignableFrom(mActivity
                .getClass()) ? ((BaseActivity) mActivity).getNavigationTmp()
                : null;

        String navigation = navigationTmp != null ? navigationTmp
                : mActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS)
                .getString(Constants.PREF_NAVIGATION,
                        navigationListCodes[0]);

        if (navigation.equals(String.valueOf(categories.get(position).getId()))) {
            return true;
        } else {
            return false;
        }
    }

}


/**
 * Holder object
 *
 * @author fede
 */
class NoteDrawerCategoryAdapterViewHolder {

    ImageView imgIcon;
    TextView txtTitle;
    android.widget.TextView count;
}
