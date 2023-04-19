package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class Owner_ProDisc extends AppCompatActivity {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText prodiscTitle, prodiscDescription;
    ImageButton prodiscImage;
    Button cancel, add;
    BottomNavigationView navigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_pro_disc);

        useBottomNavigationMenu();

        floatingActionButton = findViewById(R.id.addFloatButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });


    }

    public void useBottomNavigationMenu(){
        //Navigation Bar------------------------------------------
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Toast.makeText(Owner_ProDisc.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Owner_ProDisc.this, OwnerHomepage.class));
                        break;
                    case R.id.menuMessages:
                        Toast.makeText(Owner_ProDisc.this, "Messages", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuNotification:
                        Toast.makeText(Owner_ProDisc.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuProfile:
                        Toast.makeText(Owner_ProDisc.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });//Navigation Bar------------------------------------------
    }

    //dialog box for adding a new promotion or discount (pop up dialog)
    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View prodiscPopupview = getLayoutInflater().inflate(R.layout.popout_prodisc_add, null);
        prodiscImage = (ImageButton) prodiscPopupview.findViewById(R.id.prodiscImage_imageButton_popup);
        prodiscTitle = (EditText) prodiscPopupview.findViewById(R.id.prodiscTitle_editText_popup);
        prodiscDescription = (EditText) prodiscPopupview.findViewById(R.id.prodiscDesc_editText_popup);

        cancel = (Button) prodiscPopupview.findViewById(R.id.prodisc_cancelButton);
        add = (Button) prodiscPopupview.findViewById(R.id.prodisc_addButton);

        dialogBuilder.setView(prodiscPopupview);
        dialog = dialogBuilder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: set code on how to add data to firestore


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Toast.makeText(Owner_ProDisc.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }


}