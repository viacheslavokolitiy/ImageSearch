package me.imagesearch;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.adapter.FavouritesAdapter;
import me.imagesearch.adapter.PhotosAdapter;
import me.imagesearch.db.ImageContract;
import me.imagesearch.model.PhotoItem;
import me.imagesearch.utils.Constants;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class FavouritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @InjectView(R.id.favourites)
    protected ListView favourites;
    private String cursorFilter;
    private SimpleCursorAdapter simpleCursorAdapter;
    private SimpleCursorAdapter scAdapter;
    private boolean needAddItems;

    public static FavouritesFragment newInstance(int fragmentPosition){
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.PAGE, fragmentPosition);
        FavouritesFragment fragment = new FavouritesFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.inject(this, view);
        getActivity().getSupportLoaderManager().initLoader(2, null, this).forceLoad();
        favourites.setOnScrollListener(onScrollListener);
        return view;
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            final int lastItem = firstVisibleItem + visibleItemCount;
            if(lastItem == totalItemCount) {
                addNewItems(true);
            }
        }
    };

    private void addNewItems(boolean b) {
        this.needAddItems = b;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ImagesCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<PhotoItem> photoItemList = new ArrayList<>(10);
        if(data != null){
            if(data.getCount() > 0 && data.moveToFirst()){
                do {
                    String imageUrl = data.getString(data.getColumnIndex(ImageContract.IMAGE_URL));
                    String title = data.getString(data.getColumnIndex(ImageContract.TITLE));
                    PhotoItem item = new PhotoItem();
                    item.setTitle(title);
                    item.setPhotoURL(imageUrl);
                    if(!needAddItems){
                        if(!photoItemList.contains(item) && photoItemList.size() <= 10) {
                            photoItemList.add(item);
                        }
                    } else {
                        if(!photoItemList.contains(item)) {
                            photoItemList.add(item);
                        }
                    }


                } while (data.moveToNext());
            }

            favourites.setAdapter(new FavouritesAdapter(getActivity(), photoItemList));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class ImagesCursorLoader extends CursorLoader {
        private Cursor mCursor;
        private Context mContext;

        public ImagesCursorLoader(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public Cursor loadInBackground() {
            mCursor = mContext.getContentResolver().query(ImageContract.CONTENT_URI, new String[]{
                    ImageContract.IMAGE_URL,
                    ImageContract.TITLE
            }, null, null, null);

            return mCursor;
        }

        @Override
        public void deliverResult(Cursor cursor) {
            super.deliverResult(cursor);
        }
    }
}
