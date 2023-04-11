package com.example.caspaceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void transactionButton(View view){
        LayoutInflater layoutInflater =(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupwindow = layoutInflater.inflate(R.layout.activity_transactionhistory, null);
        PopupWindow popupWindow = new PopupWindow(viewPopupwindow, 900, 500, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}