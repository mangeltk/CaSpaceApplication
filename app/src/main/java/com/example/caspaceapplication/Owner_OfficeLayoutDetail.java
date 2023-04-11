package com.example.caspaceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Owner_OfficeLayoutDetail extends AppCompatActivity {

    TextView detailPeople, detailName, detailAreasize;
    ImageView detailImage;
    Button editDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layout_detail);

        detailName = findViewById(R.id.detailName);
        detailPeople = findViewById(R.id.detailPeopleAnswer);
        detailAreasize = findViewById(R.id.detailAreasizeAnswer);
        detailImage = findViewById(R.id.detailImage);

        editDetailsButton = findViewById(R.id.editLayoutDetails);

        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            detailImage.setImageResource(bundle.getInt("Image"));
            detailName.setText(bundle.getString("Title"));
            detailPeople.setText(bundle.getString("No.of People"));
            detailAreasize.setText(bundle.getString("Area size"));
        }

        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficelayoutsRegistration.class));
            }
        });

    }
}