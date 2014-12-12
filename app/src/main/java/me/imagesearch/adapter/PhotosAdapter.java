package me.imagesearch.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.R;
import me.imagesearch.db.ImageContract;
import me.imagesearch.model.PhotoItem;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class PhotosAdapter extends ArrayAdapter<PhotoItem> {
    private Context mContext;
    private List<PhotoItem> items;
    private LayoutInflater mInflater;

    public PhotosAdapter(Context context, List<PhotoItem> objects) {
        super(context, R.layout.row_image_item, objects);
        this.mContext = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        ViewHolder viewHolder;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = mInflater.inflate(R.layout.row_image_item, parent, false);
        viewHolder = new ViewHolder(rowView);
        rowView.setTag(viewHolder);

        final PhotoItem image = getItem(position);
        Picasso.with(mContext).load(image.getPhotoURL()).into(viewHolder.mLogo);
        Picasso.with(mContext).load(image.getPhotoURL()).into(target);
        viewHolder.mTitle.setText(Html.fromHtml(image.getTitle()));
        viewHolder.mAddToFavourites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContentValues values = new ContentValues();
                values.put(ImageContract.IMAGE_URL, image.getPhotoURL());
                values.put(ImageContract.TITLE, image.getTitle());
                mContext.getContentResolver().insert(ImageContract.CONTENT_URI, values);
                Toast.makeText(mContext, mContext.getString(R.string.text_added_toast), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

    private Target target = new Target(){

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/ImageSearch/saved_image"+ Double.toString(Math.random()) +".jpg");

            try
            {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                ostream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    class ViewHolder {
        @InjectView(R.id.logo)
        protected ImageView mLogo;
        @InjectView(R.id.image_text)
        protected TextView mTitle;
        @InjectView(R.id.check_add_to_fav)
        protected CheckBox mAddToFavourites;

        ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }
}
