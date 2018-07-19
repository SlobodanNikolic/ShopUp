package cobe.com.bejbikjum.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cobe.com.bejbikjum.R;

public class UploadPhotoActivity extends AppCompatActivity {

    public ImageView[] imageViews = new ImageView[4];
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_upload_photo);

        imageViews[0]=(ImageView)findViewById(R.id.image1);
        imageViews[1]=(ImageView)findViewById(R.id.image2);
        imageViews[2]=(ImageView)findViewById(R.id.image3);
        imageViews[3]=(ImageView)findViewById(R.id.image4);

        for (ImageView image: imageViews) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChooser();
                }
            });
        }

    }


    public void showChooser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Take a photo, or choose one from the gallery")
                .setCancelable(true)
                .setPositiveButton("Photo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(Build.VERSION.SDK_INT >= 21) {
                            Intent addPhotoIntent = new Intent(getApplicationContext(), TakePhotoActivity.class);
                            startActivity(addPhotoIntent);
                        }
                        else{
                            Log.d("upload", "Device SDK API lower than 21");
                        }
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICK_IMAGE);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
        }
    }

}
