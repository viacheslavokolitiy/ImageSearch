package me.imagesearch.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.R;
import me.imagesearch.utils.Constants;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class FullScreenActivity extends ActionBarActivity {
    @InjectView(R.id.full_image)
    protected ImageView fullScreenImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        String receivedURL = bundle.getString(Constants.PHOTO_URL_SELECTED);
        Picasso.with(this).load(receivedURL).fit().centerCrop().into(fullScreenImage);
        getSupportActionBar().hide();
    }
}
