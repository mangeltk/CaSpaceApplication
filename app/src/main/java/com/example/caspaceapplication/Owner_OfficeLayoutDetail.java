package com.example.caspaceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

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
            String imagePath = getIntent().getStringExtra("layoutImage");
                Picasso.get().load(imagePath).into(detailImage);
            detailName.setText(bundle.getString("layoutName"));
            detailPeople.setText(bundle.getString("layoutPeopleNum"));
            detailAreasize.setText(bundle.getString("layoutAreasize"));
        }

        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficelayoutsRegistration.class));
            }
        });

    }
}