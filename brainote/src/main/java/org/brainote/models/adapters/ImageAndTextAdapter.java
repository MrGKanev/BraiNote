package org.brainote.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.brainote.utils.Constants;
import org.brainote.utils.Fonts;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import org.brainote.R;
import org.brainote.models.holders.ImageAndTextItem;


public class ImageAndTextAdapter extends BaseAdapter {

    private Activity mActivity;
    ArrayList<ImageAndTextItem> items;
    private LayoutInflater inflater;


    public ImageAndTextAdapter(Activity mActivity,
                               ArrayList<ImageAndTextItem> items) {
        this.mActivity = mActivity;
        this.items = items;
        inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageAndTextViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_and_text_item,
                    parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, mActivity.getSharedPreferences(
                            Constants.PREFS_NAME, mActivity.MODE_MULTI_PROCESS),
                    convertView);

            holder = new ImageAndTextViewHolder();

            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ImageAndTextViewHolder) convertView.getTag();
        }

        // Set the results into TextViews
        holder.text.setText(items.get(position).getText());

        // Set the results into ImageView checking if an icon is present before
        if (items.get(position).getImage() != 0) {
            holder.image.setImageResource(items.get(position).getImage());
        }

        return convertView;
    }

}


/**
 * Holder object
 *
 * @author fede
 */
class ImageAndTextViewHolder {

    ImageView image;
    TextView text;
}
