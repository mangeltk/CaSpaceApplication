package com.example.caspaceapplication.Owner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;

public class Owner_ProDisc extends AppCompatActivity {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText prodiscTitle, prodiscDescription;
    ImageButton prodiscImage;
    Button cancel, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_pro_disc);

        createNewDialog();
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

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}