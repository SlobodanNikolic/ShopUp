package cobe.com.bejbikjum.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.SlidingImageAdapter;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.helpers.MyGlideApp;
import cobe.com.bejbikjum.models.Item;

public class ImageDetailsActivity extends AppCompatActivity {

    @BindView(R.id.image_view_pager)
    ViewPager imagePager;
    private Item item;

    String imageUrl;
    @BindView(R.id.product_name)
    TextView nameTextView;
    @BindView(R.id.product_price)
    TextView priceTextView;
    @BindView(R.id.materials)
    TextView materialsTextView;
    @BindView(R.id.sizes)
    TextView sizesTextView;
    @BindView(R.id.details_content)
    TextView detailsTextView;
    @BindView(R.id.hearth_button)
    ImageView hearthButton;
    @BindView(R.id.comment_button)
    ImageView commentButton;
    @BindView(R.id.shop_name)
    TextView shopName;

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

        Bundle data = getIntent().getExtras();
        item = (Item) data.getParcelable("item");

        nameTextView.setText(item.getName());
        priceTextView.setText(item.getPrice()+"");
        materialsTextView.setText(item.getMaterialString());
        detailsTextView.setText(item.getDescription());
        shopName.setText(item.getShopName());

        ArrayList<String> images = getIntent().getStringArrayListExtra("items");
        imagePager.setAdapter(new SlidingImageAdapter(ImageDetailsActivity.this, item));

        setListeners();

        hearthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseControler.getInstance().likeItem(item);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        setListeners();
    }

    public void setListeners(){
        FirebaseControler.getInstance().setDownloadListener(new FirebaseControler.DownloadListener() {


            @Override
            public void onTopRated(ArrayList<Item> topRatedItems) {

            }

            @Override
            public void onTopRatedFailed() {

            }

            @Override
            public void onNewItems(ArrayList<Item> newItems) {

            }

            @Override
            public void onNewItemsFailed() {

            }

            @Override
            public void onItemLiked() {
                hearthButton.setImageResource(R.drawable.heart_full);
            }

            @Override
            public void onItemLikeFailed() {
                // TODO: 9/20/18 Implementirati odgovarajucu akciju
            }

            @Override
            public void onRandomItems(ArrayList<Item> randomItems) {

            }

            @Override
            public void onRandomItemsFailed() {

            }
        });
    }

}
