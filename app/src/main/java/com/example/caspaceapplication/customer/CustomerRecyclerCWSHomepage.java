package com.example.caspaceapplication.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;

public class CustomerRecyclerCWSHomepage extends AppCompatActivity {

    TextView cwsName, cwsDesc, cwsAnother;
    ImageView uploadImage, cwsImage;
    Button submitButton;
    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_recycler_cwshomepage);

        cwsName = findViewById(R.id.cwsName);
        cwsDesc = findViewById(R.id.cwsDesc);
        cwsAnother = findViewById(R.id.cwsAnother);
        uploadImage = findViewById(R.id.uploadImage);
        cwsImage = findViewById(R.id.cwsImage);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UploadImage();
            }
        });
    }

    /*private void UploadImage() {

                .withPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(CustomerRecyclerCWSHomepage.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK)
        {
            ImageUri = data.getData();
            cwsImage.setImageURI(ImageUri);
        }
    }
}