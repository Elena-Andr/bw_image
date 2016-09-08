package com.elenaa.black_and_whiteimage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // A request code for picking image from a gallery.
    private static final int REQUEST_CODE_OPEN_PHOTO = 1;

    // Loaded image Uri
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View Pager
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.modes_ViewPager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    // Handle "Open image from gallery" button click
    public void onOpenImageButtonClick(View view) {
        final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_OPEN_PHOTO);
    }

    @Override
    protected void onActivityResult( final int requestCode, final int resultCode, final Intent data ) {

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_OPEN_PHOTO){
            mImageUri = data.getData();
        }

        // Update image views' of all existing fragments
        if(mImageUri != null){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if(fragments != null) {
                for (Fragment fragment : fragments) {
                    if(fragment != null) {
                        FragmentBase myFragment = (FragmentBase) fragment;
                        myFragment.refreshImageView(mImageUri);
                    }
                }
            }
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        private static final int PAGES_COUNT = 3;

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = Fragment.instantiate(MainActivity.this, FragmentBefore.class.getName());
                    break;
                case 1:
                    fragment = Fragment.instantiate(MainActivity.this, FragmentAfter.class.getName());
                    break;
                case 2:
                    fragment = Fragment.instantiate(MainActivity.this, FragmentBeforeAfter.class.getName());
                    break;
                default:
                    Log.e(LOG_TAG, "Error when creating fragment: unsupported fragment position");
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            String resourceString = "page_" + position;
            int resourceId = getResources().getIdentifier(resourceString, "string", getPackageName());
            return getString(resourceId);

        }
    }
}
