package com.elenaa.black_and_whiteimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.elenaa.black_and_whiteimage.processor.FiltersEnum;
import com.elenaa.black_and_whiteimage.utils.BitmapWorkerTask;

import java.io.IOException;
import java.io.InputStream;

public class FragmentBeforeAfter extends FragmentBase {

    private static final String LOG_TAG = FragmentBeforeAfter.class.getSimpleName();

    // UI components
    private ImageView mBeforeImageView;
    private ImageView mAfterImageView;
    private Spinner mFilterSpinner;

    // Specifies currently selected filter
    private FiltersEnum mFilterSelected;
	
	// Input image Uri
    private Uri mOriginalImageUri;

    // This part of the image should be processed
    private Rect mRightHalfImageRect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_before_after, container, false);

        mBeforeImageView = (ImageView)rootView.findViewById(R.id.half_before_imageView);
        mAfterImageView = (ImageView)rootView.findViewById(R.id.half_after_imageView);

        Button runButton = (Button)rootView.findViewById(R.id.run_button);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOriginalImageUri != null) {

                    if(mFilterSpinner.getSelectedItemId() == 0){
                        Toast.makeText(getActivity(), R.string.filter_hint, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(getActivity(), mAfterImageView, mRightHalfImageRect);
                    bitmapWorkerTask.setFilter(mFilterSelected);
                    bitmapWorkerTask.execute(mOriginalImageUri);

                }else
                    Toast.makeText(getActivity(), R.string.open_image_hint, Toast.LENGTH_SHORT).show();
                }
        });

        // Initialize filter spinner in the UI with available filters
        mFilterSpinner = (Spinner)rootView.findViewById(R.id.filters_spinner);
        String[] availableFilters = getResources().getStringArray(R.array.available_filters);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                availableFilters);
        mFilterSpinner.setAdapter(adapter);

        //Set no filter by default
        mFilterSpinner.setSelection(0);

        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterSelected = FiltersEnum.getFilterById(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return  rootView;
    }

    @Override
    public void refreshImageView(Uri imageUri){

        // Update image uri
        mOriginalImageUri = imageUri;

        //Set no filter by default
        mFilterSpinner.setSelection(0);

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(mOriginalImageUri);

            // Get only width and height of the image
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(inputStream, null, options);

            // Calculate rects of the image's halves
            Rect leftHalfImageRect = new Rect(0, 0, options.outWidth/2, options.outHeight);
            mRightHalfImageRect = new Rect(options.outWidth/2, 0, options.outWidth, options.outHeight);

            // Load image parts to the image views
            BitmapWorkerTask bitmapWorkerTaskLeft = new BitmapWorkerTask(getActivity(), mBeforeImageView, leftHalfImageRect);
            bitmapWorkerTaskLeft.execute(mOriginalImageUri);

            BitmapWorkerTask bitmapWorkerTaskRight = new BitmapWorkerTask(getActivity(), mAfterImageView, mRightHalfImageRect);
            bitmapWorkerTaskRight.execute(mOriginalImageUri);

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}
