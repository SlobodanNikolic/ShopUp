package cobe.com.bejbikjum.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.Console;
import java.util.Arrays;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.ProductTypeAdapter;
import cobe.com.bejbikjum.controlers.AppControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;

public class UploadPhotoActivity extends AppCompatActivity implements ProductTypeAdapter.ItemClickListener{

    public ImageView[] imageViews = new ImageView[4];
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    public int imageViewSelected = 0;
    ProductTypeAdapter adapter;


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
        FirebaseControler.getInstance().setContext(this, this);

        imageViews[0]=(ImageView)findViewById(R.id.image1);
        imageViews[1]=(ImageView)findViewById(R.id.image2);
        imageViews[2]=(ImageView)findViewById(R.id.image3);
        imageViews[3]=(ImageView)findViewById(R.id.image4);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_types_recycler);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ProductTypeAdapter(this, AppControler.getInstance().getData(), AppControler.getInstance().getIconIds());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        imageViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewSelected = 0;
                showChooser();
            }
        });

        imageViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewSelected = 1;
                showChooser();
            }
        });

        imageViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewSelected = 2;
                showChooser();
            }
        });

        imageViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewSelected = 3;
                showChooser();
            }
        });


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
                            Intent addPhotoIntent = new Intent(getApplicationContext(), TakePhotoLowActivity.class);
                            startActivity(addPhotoIntent);
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
            if(data!=null) {
                Uri selectedImage = data.getData();
                imageViews[imageViewSelected].setImageURI(selectedImage);
            }

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("ShopInfo", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Log.d("ShopInfo", AppControler.getInstance().getCurrentSeller().getItemTypes().toString());
    }

}
