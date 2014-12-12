package me.imagesearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.imagesearch.ui.SearchFragment;


public class MainActivity extends ActionBarActivity {
    private static final int SEARCH_FRAGMENT = 0;
    private static final int FAVOURITES_FRAGMENT = 1;
    @InjectView(R.id.pager_strip)
    protected PagerTabStrip tabStrip;
    @InjectView(R.id.viewpager)
    protected ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        viewPager.setAdapter(new AppFragmentPagerAdapter(getSupportFragmentManager()));
    }

    private class AppFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;

        public AppFragmentPagerAdapter(FragmentManager fm) {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case SEARCH_FRAGMENT:
                    return SearchFragment.newInstance(position);
                case FAVOURITES_FRAGMENT:
                    return FavouritesFragment.newInstance(position);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case SEARCH_FRAGMENT:
                    return "Search";
                case FAVOURITES_FRAGMENT:
                    return "Favourites";
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
