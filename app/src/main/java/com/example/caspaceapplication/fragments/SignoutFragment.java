package com.example.caspaceapplication.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.caspaceapplication.customer.Front;
import com.example.caspaceapplication.R;


public class SignoutFragment extends Fragment {

    private float userRate = 0;

    AppCompatButton rateNowButton, rateLaterButton;
    ImageView ratingImage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AppCompatButton rateNowButton = view.findViewById(R.id.rateNowButton);
        final AppCompatButton rateLaterButton = view.findViewById(R.id.rateLaterButton);
        final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        final ImageView ratingImage = view.findViewById(R.id.ratingImage);

        /*final AppCompatButton exitNowButton = view.findViewById(R.id.exitNowButton);
        final AppCompatButton maybeLaterButton = view.findViewById(R.id.maybeLaterButton);*/
        /*final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        final ImageView ratingImage = view.findViewById(R.id.ratingImage);*/

        rateNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle rate now button click
            }
        });

        rateLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Front.class);
                startActivity(intent);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating <= 1) {
                    ratingImage.setImageResource(R.drawable.onestar);
                } else if (rating <= 2) {
                    ratingImage.setImageResource(R.drawable.twostar);
                } else if (rating <= 3) {
                    ratingImage.setImageResource(R.drawable.threestar);
                } else if (rating <= 4) {
                    ratingImage.setImageResource(R.drawable.fourstar);
                } else if (rating <= 5) {
                    ratingImage.setImageResource(R.drawable.fivestar);
                }

                //animate emoji image
                animatedImage(ratingImage);
                //selected rating by user
                userRate = rating;
            }

        });

    }

    private void dismiss() {
    }

    private void animatedImage(ImageView ratingImage) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signout, container, false);

    }
}