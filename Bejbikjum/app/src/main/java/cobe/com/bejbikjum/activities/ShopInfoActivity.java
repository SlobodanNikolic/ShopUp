package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.activities.HomeActivity;
import cobe.com.bejbikjum.activities.MyShopActivity;
import cobe.com.bejbikjum.adapters.ProductTypeAdapter;
import cobe.com.bejbikjum.controlers.AppControler;
import cobe.com.bejbikjum.controlers.FacebookControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.models.Seller;

public class ShopInfoActivity extends AppCompatActivity implements ProductTypeAdapter.ItemClickListener {


    ProductTypeAdapter adapter;
    @BindView(R.id.continue_button)
    Button continueButton;
    @BindView(R.id.shop_name)
    EditText shopNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_shop_info);
        ButterKnife.bind(this);

        FirebaseControler.getInstance().setContext(this, this);

        String[] data = {"Women's shoes", "Men's shoes", "Sneakers", "Handbags", "Dresses", "Sports", "Men's jeans", "Women's jeans",
        "Men's suits", "Men's underwear", "Lingerie", "Skirts", "Men's hats", "Sunglasses", "Accessories"};
        int[] iconIds = {R.drawable.high_heels, R.drawable.shoes, R.drawable.running, R.drawable.handbag, R.drawable.dress,
                R.drawable.sport, R.drawable.jeansm, R.drawable.jeansw, R.drawable.suit, R.drawable.boxer, R.drawable.underwear,
                R.drawable.skirt, R.drawable.hatm, R.drawable.sunglasses, R.drawable.jewel};

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_types_recycler);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ProductTypeAdapter(this, data, iconIds);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //For opening the myShop screen
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {
                    Seller currentSeller = AppControler.getInstance().getCurrentSeller();
                    FirebaseUser currentUser = FirebaseControler.getInstance().getCurrentUser();

                    if(currentUser != null){
                        currentSeller = new Seller(currentUser.getUid(), shopNameInput.getText().toString(), currentUser.getEmail(), "",
                                FacebookControler.getInstance().getToken().getUserId(), currentUser.getDisplayName(), "", currentSeller.getItemTypes());
                        FirebaseControler.getInstance().addFirestoreSeller(null, currentSeller, "");
                    }
                    else{
                        FirebaseControler.getInstance().createSellerAccount(currentSeller.getEmail(), currentSeller.getPassword());
                    }
//                    Intent myShopIntent = new Intent(getApplicationContext(), MyShopActivity.class);
//                    startActivity(myShopIntent);
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("ShopInfo", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Log.d("ShopInfo", AppControler.getInstance().getCurrentSeller().getItemTypes().toString());
    }


    private boolean validate() {
        boolean valid = true;

        String shopName = shopNameInput.getText().toString();
        if (TextUtils.isEmpty(shopName)) {
            shopNameInput.setError("Required.");
            valid = false;
        } else {
            shopNameInput.setError(null);
        }

        return valid;
    }


}
