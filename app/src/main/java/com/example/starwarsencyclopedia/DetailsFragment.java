package com.example.starwarsencyclopedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the arguments from the bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Update UI with data from bundle
            ((TextView) view.findViewById(R.id.nameTextView)).setText(bundle.getString("name"));
            ((TextView) view.findViewById(R.id.heightTextView)).setText(bundle.getString("height"));
            ((TextView) view.findViewById(R.id.massTextView)).setText(bundle.getString("mass"));
            ((TextView) view.findViewById(R.id.hairColorTextView)).setText(bundle.getString("hairColor"));
        }
    }
}
