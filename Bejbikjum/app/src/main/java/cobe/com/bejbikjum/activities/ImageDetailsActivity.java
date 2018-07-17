package cobe.com.bejbikjum.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.SlidingImageAdapter;
import cobe.com.bejbikjum.helpers.MyGlideApp;

public class ImageDetailsActivity extends AppCompatActivity {

    @BindView(R.id.image_view_pager)
    ViewPager imagePager;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);

        ArrayList<String> images = getIntent().getStringArrayListExtra("images");
        imagePager.setAdapter(new SlidingImageAdapter(ImageDetailsActivity.this, images));

    }

}
