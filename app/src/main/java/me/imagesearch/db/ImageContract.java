package me.imagesearch.db;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public interface ImageContract extends ProviGenBaseContract {

    @Column(Column.Type.TEXT)
    public static final String IMAGE_URL = "image_url";
    @Column(Column.Type.TEXT)
    public static final String TITLE = "image_title";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://me.imagesearch/images");
}
