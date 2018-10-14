package cobe.com.bejbikjum.activities;

import android.arch.persistence.room.Room;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import java.io.Console;
import java.util.Arrays;
import java.util.Random;

import cobe.com.bejbikjum.DAO.LocalDB;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.ProductTypeAdapter;
import cobe.com.bejbikjum.controlers.AppControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.controlers.StorageControler;
import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Material;
import cobe.com.bejbikjum.models.Size;

public class UploadPhotoActivity extends AppCompatActivity implements ProductTypeAdapter.ItemClickListener{

    public ImageView[] imageViews = new ImageView[4];
    public ImageView[] colorViews = new ImageView[10];

    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    public int imageViewSelected = 0;
    ProductTypeAdapter adapter;
    private Item currentItem;

    private Uri[] imageUris = new Uri[4];
    private LinearLayout progressLayout;
    private EditText itemNameInput;
    private EditText materialsInput;
    private EditText descriptionInput;
    private EditText priceInput;



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

        progressLayout = (LinearLayout) findViewById(R.id.progress_layout_upload);
        hideProgressBar();

        currentItem = new Item();

        imageViews[0]=(ImageView)findViewById(R.id.image1);
        imageViews[1]=(ImageView)findViewById(R.id.image2);
        imageViews[2]=(ImageView)findViewById(R.id.image3);
        imageViews[3]=(ImageView)findViewById(R.id.image4);

        colorViews[0]=(ImageView)findViewById(R.id.color1);
        colorViews[1]=(ImageView)findViewById(R.id.color2);
        colorViews[2]=(ImageView)findViewById(R.id.color3);
        colorViews[3]=(ImageView)findViewById(R.id.color4);
        colorViews[4]=(ImageView)findViewById(R.id.color5);
        colorViews[5]=(ImageView)findViewById(R.id.color6);
        colorViews[6]=(ImageView)findViewById(R.id.color7);
        colorViews[7]=(ImageView)findViewById(R.id.color8);
        colorViews[8]=(ImageView)findViewById(R.id.color9);
        colorViews[9]=(ImageView)findViewById(R.id.color10);

        materialsInput = (EditText) findViewById(R.id.materials_input);
        descriptionInput = (EditText) findViewById(R.id.item_description_input);
        priceInput = (EditText) findViewById(R.id.item_price_input);
        itemNameInput = (EditText) findViewById(R.id.item_name_input);

        Button continueButton = (Button) findViewById(R.id.upload_item_button);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_types_recycler);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ProductTypeAdapter(this, AppControler.getInstance().getData(), AppControler.getInstance().getIconIds());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        for(ImageView view : colorViews){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAllColorBorders();
                    view.setBackgroundResource(R.drawable.square_border);
                    currentItem.setColorString(view.getTag().toString());
                }
            });
        }

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

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){

                    Random r = new Random();
                    int randomNum = r.nextInt(10000);

                    currentItem = new Item("", itemNameInput.getText().toString(), Timestamp.now().getSeconds(), materialsInput.getText().toString(), descriptionInput.getText().toString(), randomNum, 0, "", "", currentItem.getColorString(),
                            AppControler.getInstance().getCurrentSeller().getUid(), AppControler.getInstance().getCurrentSeller().getShopName(), Integer.parseInt(priceInput.getText().toString()), currentItem.getItemType(),
                            0, 0, AppControler.getInstance().getCurrentSeller().getShopName());

                    StorageControler.getInstance().uploadImage(imageUris, currentItem, progressLayout);
                }
            }
        });

    }

    public void showProgressBar(){
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        progressLayout.setVisibility(View.INVISIBLE);
    }

    public void removeAllColorBorders(){
        for(ImageView image : colorViews){
            image.setBackgroundResource(0);
        }
    }

    public boolean validate(){

        boolean valid = true;

        if (TextUtils.isEmpty(itemNameInput.getText().toString())) {
            itemNameInput.setError("Required.");
            valid = false;
        } else {
            itemNameInput.setError(null);
        }

        if (TextUtils.isEmpty(priceInput.getText().toString())) {
            priceInput.setError("Required.");
            valid = false;
        } else {
            priceInput.setError(null);
        }


        return valid;
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
                imageUris[imageViewSelected] = selectedImage;
            }

        }
    }

    @Override
    public void onItemClick(View view, int position, LinearLayout productLayout, ImageView productTypeImage, TextView productTypeName, boolean selected) {
        if(selected){
            productLayout.setBackgroundResource(R.drawable.square_border);
            currentItem.setItemType(productTypeName.getText().toString());
        }
        else {
            productLayout.setBackgroundResource(0);
        }

        Log.d("ShopInfo", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Log.d("ShopInfo", AppControler.getInstance().getCurrentSeller().getItemTypes().toString());
    }

}
