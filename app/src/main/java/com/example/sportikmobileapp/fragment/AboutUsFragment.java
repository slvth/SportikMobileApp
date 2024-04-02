package com.example.sportikmobileapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.sportikmobileapp.R;


public class AboutUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        AppCompatButton btnOpenMapAboutUs = view.findViewById(R.id.btnOpenMapAboutUs);
        btnOpenMapAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://go.2gis.com/an0my";
                Intent openPage= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(openPage);
            }
        });

        return view;
    }
}