package com.elenaa.black_and_whiteimage;

import android.net.Uri;
import android.support.v4.app.Fragment;

public abstract class FragmentBase extends Fragment {

    public abstract void refreshImageView(Uri imageUri);
}
