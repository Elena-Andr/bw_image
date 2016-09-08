package com.elenaa.black_and_whiteimage;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.elenaa.black_and_whiteimage.processor.FiltersEnum;
import com.elenaa.black_and_whiteimage.utils.BitmapWorkerTask;

public class FragmentAfter extends FragmentBase {

    private static final String LOG_TAG = FragmentAfter.class.getSimpleName();

    // UI components
    private ImageView mAfterImageView;
    private Spinner mFilterSpinner;

    // Specifies currently selected filter
    private FiltersEnum mFilterSelected;

	//Input image Uri
    private Uri mOriginalImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_after, container, false);

        mAfterImageView = (ImageView) rootView.findViewById(R.id.after_imageView);

        Button runButton = (Button)rootView.findViewById(R.id.run_button);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOriginalImageUri != null) {

                    if (mFilterSpinner.getSelectedItemId() == 0) {
                        Toast.makeText(getActivity(), R.string.filter_hint, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(getActivity(), mAfterImageView);
                    bitmapWorkerTask.setFilter(mFilterSelected);
                    bitmapWorkerTask.execute(mOriginalImageUri);

                } else
                    Toast.makeText(getActivity(), R.string.open_image_hint, Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize filter spinner with available filters
        mFilterSpinner = (Spinner)rootView.findViewById(R.id.filters_spinner);
        String[] availableFilters = getResources().getStringArray(R.array.available_filters);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                availableFilters);
        mFilterSpinner.setAdapter(adapter);

        //Set no filter by default
        mFilterSpinner.setSelection(0);

        mFilterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterSelected = FiltersEnum.getFilterById(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }
    @Override
    public void refreshImageView(Uri imageUri) {

        // Update image uri
        mOriginalImageUri = imageUri;

        //Update UI
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(getActivity(), mAfterImageView);
        bitmapWorkerTask.execute(mOriginalImageUri);

        mFilterSpinner.setSelection(0);
    }
}
