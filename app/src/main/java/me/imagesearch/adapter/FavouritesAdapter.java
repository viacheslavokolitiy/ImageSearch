package me.imagesearch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.R;
import me.imagesearch.model.PhotoItem;
import me.imagesearch.utils.cache.FileCache;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class FavouritesAdapter extends ArrayAdapter<PhotoItem> {
    private Context mContext;
    private List<PhotoItem> items;
    private LayoutInflater mInflater;

    public FavouritesAdapter(Context context, List<PhotoItem> objects) {
        super(context, R.layout.row_favourites, objects);
        this.mContext = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        ViewHolder viewHolder;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = mInflater.inflate(R.layout.row_favourites, parent, false);
        viewHolder = new ViewHolder(rowView);
        rowView.setTag(viewHolder);

        final PhotoItem image = getItem(position);
        Picasso.with(mContext).load(image.getPhotoURL()).into(viewHolder.mLogo);
        viewHolder.mTitle.setText(Html.fromHtml(image.getTitle()));

        return rowView;
    }

    class ViewHolder {
        @InjectView(R.id.logo_favourite)
        protected ImageView mLogo;
        @InjectView(R.id.image_text_favourite)
        protected TextView mTitle;

        ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }
}
