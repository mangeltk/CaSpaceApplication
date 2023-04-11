package com.example.caspaceapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class RateUsDialog extends Dialog {

    private float userRate = 0;


    public RateUsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog);

        final AppCompatButton rateNowButton = findViewById(R.id.rateNowButton);
        final AppCompatButton rateLaterButton = findViewById(R.id.rateLaterButton);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final ImageView ratingImage = findViewById(R.id.ratingImage);

        rateNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rateLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
                userRate=rating;
            }
        });



    }


    private void animatedImage(ImageView ratingImage)
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }


}
