package com.elenaa.black_and_whiteimage;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elenaa.black_and_whiteimage.utils.BitmapWorkerTask;

public class FragmentBefore extends FragmentBase {

    private ImageView mBeforeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_before, container, false);
        mBeforeImageView = (ImageView) rootView.findViewById(R.id.before_imageView);

        return rootView;
    }

    @Override
    public void refreshImageView(Uri imageUri) {

        // Update image view
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(getActivity(), mBeforeImageView);
        bitmapWorkerTask.execute(imageUri);
    }
}
