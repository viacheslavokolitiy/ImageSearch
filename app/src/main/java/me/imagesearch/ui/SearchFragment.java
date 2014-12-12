package me.imagesearch.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.R;
import me.imagesearch.adapter.PhotosAdapter;
import me.imagesearch.model.PhotoItem;
import me.imagesearch.utils.Constants;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<List<PhotoItem>>, AdapterView.OnItemClickListener {
    @InjectView(R.id.images_list)
    protected ListView mImagesList;
    private SearchView searchView;
    private static String searchString;
    private JSONObject mReceivedJSON;
    private List<PhotoItem> mPhotos;

    public static SearchFragment newInstance(int id) {
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.PAGE, id);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(arguments);

        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, view);
        mImagesList.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        if (searchView.isActivated()) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            getActivity().getActionBar().setHomeButtonEnabled(true);
        }
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        this.searchString = s;
        if (searchString.length() >= 3) {
           getLoaderManager().initLoader(1, null, this).forceLoad();
        }
        return true;
    }

    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncImageLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {
        if(data != null){
            this.mPhotos = data;
            PhotosAdapter adapter = new PhotosAdapter(getActivity(), data);
            mImagesList.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), FullScreenActivity.class);
        List<String> urls = new ArrayList<>();
        for(PhotoItem item : mPhotos){
            urls.add(item.getPhotoURL());
        }

        String selectedURL = urls.get(position);
        intent.putExtra(Constants.PHOTO_URL_SELECTED, selectedURL);
        startActivity(intent);
    }

    private static class AsyncImageLoader extends AsyncTaskLoader<List<PhotoItem>> {
        private Context mContext;
        private URL mRequestedURL;
        private JSONObject mJSON;
        private ArrayList<PhotoItem> photos;
        private PhotoItem photo;
        private List<PhotoItem> listImages;

        public AsyncImageLoader(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public List<PhotoItem> loadInBackground() {
            try {
                Log.e(SearchFragment.class.getSimpleName(), "STARTED0");
                mRequestedURL = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                "v=1.0&q="+searchString+"&rsz=8");
                URLConnection connection = mRequestedURL.openConnection();
                connection.addRequestProperty("Referer", "http://localhost");
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                mJSON = new JSONObject(builder.toString());
                JSONObject responseObject = mJSON.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");
                photos = parseResult(resultArray);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return photos;
        }

        private ArrayList<PhotoItem> parseResult(JSONArray resultArray) {
            listImages = new ArrayList<>();
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj;
                try {
                    obj = resultArray.getJSONObject(i);
                    photo = new PhotoItem();

                    photo.setTitle(obj.getString("title"));
                    photo.setPhotoURL(obj.getString("tbUrl"));


                    listImages.add(photo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return (ArrayList<PhotoItem>) listImages;
        }

        @Override
        public void deliverResult(List<PhotoItem> data) {
            super.deliverResult(data);
        }
    }
}
